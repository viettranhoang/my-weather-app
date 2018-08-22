package com.vit.myweatherapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.HourWeatherResponse;
import com.vit.myweatherapp.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HourWeatherAdapter extends RecyclerView.Adapter<HourWeatherAdapter.HourWeatherViewHolder> {

    private List<HourWeatherResponse.Weather_list> weatherList = new ArrayList<>();

    private Context context;

    @NonNull
    @Override
    public HourWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour_weather, parent, false);
        return new HourWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourWeatherViewHolder holder, int position) {
        holder.bindData(weatherList.get(position));
        holder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String info = String.format("%s - %s", weatherList.get(position).getWeather().get(0).getMain(),
                                        weatherList.get(position).getWeather().get(0).getDescription());
                Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public HourWeatherAdapter(List<HourWeatherResponse.Weather_list> weatherList, Context context) {
        this.weatherList = weatherList;
        this.context = context;
    }

    public HourWeatherAdapter() {

    }


   /* public void swapData(List<HourWeatherResponse.Weather_list> data) {
        this.weatherList.clear();
        this.weatherList.addAll(data);
        notifyDataSetChanged();
    }*/

    class HourWeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_hour_weather)
        TextView mTextHourWeather;

        @BindView(R.id.text_hour_temp)
        TextView mTextHourTemp;

        @BindView(R.id.image_hour_weather)
        ImageView mImageHourWeather;

        OnItemClickListener itemListener;

        public HourWeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setOnItemClickListener(OnItemClickListener itemListener) {
            this.itemListener = itemListener;
        }

        void bindData(HourWeatherResponse.Weather_list list) {
            try {
                String weather = itemView.getResources().getString(R.string.hour_weather,
                        Utils.getDate(list.getDt()),
                        list.getWeather().get(0).getDescription(),
                        list.getWind().getSpeed(),
                        list.getMain().getPressure(),
                        list.getMain().getHumidity());
                Spanned styledText = Html.fromHtml(weather);

                mTextHourWeather.setText(styledText);
                mTextHourTemp.setText(Utils.getTempCelcius(list.getMain().getTemp()));
                Utils.getImageUrl(mTextHourWeather.getContext(), mImageHourWeather, list.getWeather().get(0).getIcon());
            } catch (Exception e) {
                Timber.e(e);
            }
        }

        @Override
        public void onClick(View v) {
            itemListener.onClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
