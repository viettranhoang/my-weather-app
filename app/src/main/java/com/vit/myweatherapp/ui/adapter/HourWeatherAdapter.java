package com.vit.myweatherapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.vit.myweatherapp.ui.feature.detail.DetailActivity;
import com.vit.myweatherapp.ui.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HourWeatherAdapter extends RecyclerView.Adapter<HourWeatherAdapter.HourWeatherViewHolder> {

    private List<HourWeatherResponse.Weather_list> weatherList = new ArrayList<>();

    private Activity activity;

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
                Intent intent = new Intent(activity, DetailActivity.class);
//                String info = String.format("%s %s")
                intent.putExtra("item", weatherList.get(position).toString());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public HourWeatherAdapter(List<HourWeatherResponse.Weather_list> weatherList, Activity activity) {
        this.weatherList = weatherList;
        this.activity = activity;
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
