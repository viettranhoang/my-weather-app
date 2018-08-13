package com.vit.myweatherapp.ui.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.HourWeatherResponse;
import com.vit.myweatherapp.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

public class HourWeatherAdapter extends RecyclerView.Adapter<HourWeatherAdapter.HourWeatherViewHolder> {

    private List<HourWeatherResponse.List> weatherList = new ArrayList<>();

    @NonNull
    @Override
    public HourWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour_weather, parent, false);
        return new HourWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourWeatherViewHolder holder, int position) {
        holder.bindData(weatherList.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public HourWeatherAdapter(List<HourWeatherResponse.List> weatherList) {
        this.weatherList = weatherList;
    }

    /**
     * when data changed
     * @param data
     */
    public void swapData(List<HourWeatherResponse.List> data) {
        this.weatherList.clear();
        this.weatherList.addAll(data);
        notifyDataSetChanged();
    }

    class HourWeatherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_hour_weather)
        TextView mTextHourWeather;

        @BindView(R.id.text_hour_temp)
        TextView mTextHourTemp;

        @BindView(R.id.image_hour_weather)
        ImageView mImageHourWeather;

        View view;

        public HourWeatherViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);

        }

        void bindData(HourWeatherResponse.List list) {
            try {
                String weather = itemView.getResources().getString(R.string.hour_weather,
                        list.getDtTxt(),
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
    }
}
