package com.vit.myweatherapp.ui.feature.main;

import android.annotation.SuppressLint;
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
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HourWeatherFragment extends BaseFragment {

    private WeatherService mWeatherService;

    private HourWeatherAdapter mAdapter;

    private int mDate;

    private List<List<HourWeatherResponse.List>> mList;

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
        View view = inflater.inflate(R.layout.fragment_hour_weather, container, false);
        mWeatherService = ApiUtils.getWeatherService();
        getData();
        return view;
    }

    private void initRcvWeather() {
        mRcvWeather.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRcvWeather.setHasFixedSize(true);
        mRcvWeather.setItemAnimator(null);
        mRcvWeather.setAdapter(mAdapter);
    }

    private void getData() {
        mWeatherService.getHourWeatherRespone(35, 139, AppConfig.API_KEY)
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

    private void setAdapter() {
        if (mDate == R.string.date_today) {
            mAdapter = new HourWeatherAdapter(mList.get(0));
        } else if (mDate == R.string.date_tomorrow) {
            mAdapter = new HourWeatherAdapter(mList.get(1));
        } else {
            mAdapter = new HourWeatherAdapter(mList.get(2));
        }
    }

    /**
     * split data for date: today, tomorrow, later
     * @param list from api
     */
    private void splitDataByDate(List<HourWeatherResponse.List> list) {
        try {
            mList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                mList.add(new ArrayList<HourWeatherResponse.List>());
            }

            int  i = 0;
            for (HourWeatherResponse.List l : list) {
                if (!Utils.getHhDate(l.getDt()).equals("22")) {
                    mList.get(i).add(l);
                } else {
                    mList.get(i).add(l);
                    if (i < 2) i++;

                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }


}
