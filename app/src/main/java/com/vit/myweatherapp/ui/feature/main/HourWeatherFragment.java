package com.vit.myweatherapp.ui.feature.main;

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

    private List<List<HourWeatherResponse.Weather_list>> mHourList;

    @BindView(R.id.list_weather)
    RecyclerView mRcvWeather;

    public HourWeatherFragment() {

    }

    public static HourWeatherFragment newInstant(int date) {
        HourWeatherFragment frm =new HourWeatherFragment();
        Bundle argBundle =new Bundle();
        argBundle.putInt("date", date);
        frm.setArguments(argBundle);
        return frm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDate = getArguments().getInt("date");

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

//        getWeatherFromCurrentLocation(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.i(this.getClass().getSimpleName() + getString(mDate) + " start");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.i(this.getClass().getSimpleName() + getString(mDate) + " stop");
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
        mWeatherService.getHourWeatherRespone(35, 139, AppConfig.API_KEY)
                .enqueue(new Callback<HourWeatherResponse>() {
                    @Override
                    public void onResponse(Call<HourWeatherResponse> call, Response<HourWeatherResponse> response) {
                        splitDataByDate(response.body().getWeather_list());
                        setAdapter();
                        initRcvWeather();
                    }

                    @Override
                    public void onFailure(Call<HourWeatherResponse> call, Throwable t) {

                    }
                });
    }

    private void getWeatherFromCity(String city) {
        mWeatherService.getHourWeatherRespone(city,  AppConfig.API_KEY)
                .enqueue(new Callback<HourWeatherResponse>() {
                    @Override
                    public void onResponse(Call<HourWeatherResponse> call, Response<HourWeatherResponse> response) {
                        Timber.i("SearchHourAPI: " + response.body().getCity().getName());
                        splitDataByDate(response.body().getWeather_list());
                        swapDataAdapter();

                    }

                    @Override
                    public void onFailure(Call<HourWeatherResponse> call, Throwable t) {
                        Timber.e("onFailure: SearchHourAPI " + t.toString());
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

    private void swapDataAdapter() {
        if (mAdapter == null) {
            mAdapter = new HourWeatherAdapter();
        }
        if (mDate == R.string.date_today) {
            mAdapter.swapData(mHourList.get(0));
        } else if (mDate == R.string.date_tomorrow) {
            mAdapter.swapData(mHourList.get(1));
        } else {
            mAdapter.swapData(mHourList.get(2));
        }
    }

    /**
     * split data for date: today, tomorrow, later
     * @param list from api
     */
    private void splitDataByDate(List<HourWeatherResponse.Weather_list> list) {
        try {
            mHourList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                mHourList.add(new ArrayList<HourWeatherResponse.Weather_list>());
            }

            int  i = 0;
            for (HourWeatherResponse.Weather_list l : list) {
                Timber.i("aaa" + i);
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
}
