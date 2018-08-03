package com.vit.myweatherapp.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "http://samples.openweathermap.org/data/";
    public static final String ICON_URL = "http://openweathermap.org/img/w/";

    public static WeatherService getWeatherService() {
        return RetrofitClient.getClient(BASE_URL).create(WeatherService.class);
    }
}
