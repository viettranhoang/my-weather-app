package com.vit.myweatherapp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.CurrentWeatherResponse;
import com.vit.myweatherapp.ui.util.Utils;


import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TodayView extends LinearLayout {

    @BindView(R.id.text_temp_today)
    TextView mTextTempToday;

    @BindView(R.id.text_weather_today)
    TextView mTextWeatherToday;

    @BindView(R.id.text_last_update)
    TextView mTextLastUpdate;

    @BindView(R.id.image_weather_today)
    ImageView mImageDay;

    public TodayView(Context context) {
        super(context);
        initialize();
    }

    public TodayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TodayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setDataForView(Context context, CurrentWeatherResponse c) {
        try {

            String weather = getResources().getString(R.string.current_weather,
                    c.getWeather().get(0).getDescription(),
                    c.getWind().getSpeed(),
                    c.getMain().getPressure(),
                    c.getMain().getHumidity(),
                    Utils.getHhMmDate(c.getSys().getSunrise()),
                    Utils.getHhMmDate(c.getSys().getSunset()));

            mTextTempToday.setText(Utils.getTempCelcius(c.getMain().getTemp()));
            mTextWeatherToday.setText(weather);
            Utils.getImageUrl(context, mImageDay, c.getWeather().get(0).getIcon());

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public void setLastUpdate() {
        try {
            String formatTime = getResources().getString(R.string.last_update, Utils.getCurrentDate());
            mTextLastUpdate.setText(formatTime);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void initialize() {
        try {
            inflate(getContext(), R.layout.view_today   , this);
            ButterKnife.bind(this);

            setLastUpdate();
        } catch (Exception e) {
            Timber.e(e);
        }
    }

}
