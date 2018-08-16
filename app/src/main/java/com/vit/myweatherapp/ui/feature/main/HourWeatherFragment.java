package com.vit.myweatherapp.ui.feature.main;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.CurrentWeatherResponse;
import com.vit.myweatherapp.data.model.HourWeatherResponse;
import com.vit.myweatherapp.data.model.SearchHourWeatherResponse;
import com.vit.myweatherapp.data.remote.ApiUtils;
import com.vit.myweatherapp.data.remote.WeatherService;
import com.vit.myweatherapp.ui.AppConfig;
import com.vit.myweatherapp.ui.adapter.HourWeatherAdapter;
import com.vit.myweatherapp.ui.base.BaseFragment;
import com.vit.myweatherapp.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HourWeatherFragment extends BaseFragment implements MainActivity.OnMainListener {

    private WeatherService mWeatherService;

    private HourWeatherAdapter mAdapter;

    private int mDate;

    private List<List<HourWeatherResponse.List>> mHourList;
    private List<List<SearchHourWeatherResponse.List>> mSearchHourList;

    @BindView(R.id.list_weather)
    RecyclerView mRcvWeather;

    public HourWeatherFragment() {

    }

    @SuppressLint("ValidFragment")
    public HourWeatherFragment(int date) {
        mDate = date;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hour_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWeatherService = ApiUtils.getWeatherService();

        ((MainActivity) getActivity()).setMainListener(this);

    }

    @Override
    public void onLocationReceived(Location location) {
        Timber.i("HourFragment:" + location.toString());
        getWeatherFromCurrentLocation(location);
    }

    @Override
    public void onInputSearchChanged(String city) {
        getWeatherFromCity(city);

    }

    @Override
    public void onCurrentWeather(Location currentLocation, CurrentWeatherResponse currentWeather) {

    }

    private void initRcvWeather() {
        mRcvWeather.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRcvWeather.setHasFixedSize(true);
        mRcvWeather.setItemAnimator(null);
        mRcvWeather.setAdapter(mAdapter);
    }

    private void getWeatherFromCurrentLocation(Location location) {
        mWeatherService.getHourWeatherRespone(location.getLatitude(), location.getLongitude(), AppConfig.API_KEY)
                .enqueue(new Callback<HourWeatherResponse>() {
                    @Override
                    public void onResponse(Call<HourWeatherResponse> call, Response<HourWeatherResponse> response) {
                        splitDataByDate(response.body().getList());
                        setAdapter();
                        initRcvWeather();
                    }

                    @Override
                    public void onFailure(Call<HourWeatherResponse> call, Throwable t) {

                    }
                });
    }

    private void getWeatherFromCity(String city) {
        mWeatherService.getSearchHourWeatherRespone(city,  AppConfig.API_KEY)
                .enqueue(new Callback<SearchHourWeatherResponse>() {
                    @Override
                    public void onResponse(Call<SearchHourWeatherResponse> call, Response<SearchHourWeatherResponse> response) {
                        Timber.i("SearchHourAPI: " + response.body().getCity().getName());
                        splitSearchDataByDate(response.body().getList());
                        setAdapter();
                        initRcvWeather();
                    }

                    @Override
                    public void onFailure(Call<SearchHourWeatherResponse> call, Throwable t) {
                        Timber.e("Error: SearchHourAPI ");
                    }
                });
    }

    private void setAdapter() {
        if (mDate == R.string.date_today) {
            mAdapter = new HourWeatherAdapter(mHourList.get(0));
        } else if (mDate == R.string.date_tomorrow) {
            mAdapter = new HourWeatherAdapter(mHourList.get(1));
        } else {
            mAdapter = new HourWeatherAdapter(mHourList.get(2));
        }
    }

    /**
     * split data for date: today, tomorrow, later
     * @param list from api
     */
    private void splitDataByDate(List<HourWeatherResponse.List> list) {
        try {
            mHourList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                mHourList.add(new ArrayList<HourWeatherResponse.List>());
            }

            int  i = 0;
            for (HourWeatherResponse.List l : list) {
                if (!Utils.getHhDate(l.getDt()).equals("22")) {
                    mHourList.get(i).add(l);
                } else {
                    mHourList.get(i).add(l);
                    if (i < 2) i++;

                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void splitSearchDataByDate(List<SearchHourWeatherResponse.List> list) {
        try {
            mSearchHourList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                mSearchHourList.add(new ArrayList<SearchHourWeatherResponse.List>());
            }

            int  i = 0;
            for (SearchHourWeatherResponse.List l : list) {
                if (!Utils.getHhDate(l.getDt()).equals("22")) {
                    mSearchHourList.get(i).add(l);
                } else {
                    mSearchHourList.get(i).add(l);
                    if (i < 2) i++;

                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }



}
