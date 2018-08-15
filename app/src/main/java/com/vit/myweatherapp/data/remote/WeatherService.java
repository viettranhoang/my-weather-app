package com.vit.myweatherapp.data.remote;

import com.vit.myweatherapp.data.model.CurrentWeatherResponse;
import com.vit.myweatherapp.data.model.DailyWeatherResponse;
import com.vit.myweatherapp.data.model.HourWeatherResponse;
import com.vit.myweatherapp.data.model.SearchHourWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("2.5/weather?")
    Call<CurrentWeatherResponse> getCurrentWeatherResponse(@Query("lat") double lat,
                                                           @Query("lon") double lon,
                                                           @Query("appid") String appid);

    @GET("2.5/weather?")
    Call<CurrentWeatherResponse> getCurrentWeatherResponse(@Query("q") String q,
                                                           @Query("appid") String appid);

    @GET("2.5/forecast/daily?")
    Call<DailyWeatherResponse> getDailyWeatherRespone(@Query("lat") double lat,
                                                      @Query("lon") double lon,
                                                      @Query("cnt") int cnt,
                                                      @Query("appid") String appid);

    @GET("2.5/forecast/daily?")
    Call<DailyWeatherResponse> getDailyWeatherRespone(@Query("q") String q,
                                                      @Query("appid") String appid);

    @GET("2.5/forecast?")
    Call<HourWeatherResponse> getHourWeatherRespone(@Query("lat") double lat,
                                                    @Query("lon") double lon,
                                                    @Query("appid") String appid);

    @GET("2.5/forecast?")
    Call<SearchHourWeatherResponse> getSearchHourWeatherRespone(@Query("q") String q,
                                                                @Query("appid") String appid);
}
