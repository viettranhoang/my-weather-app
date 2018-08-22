package com.vit.myweatherapp.ui.feature.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.HourWeatherResponse;
import com.vit.myweatherapp.ui.adapter.HourWeatherAdapter;
import com.vit.myweatherapp.ui.base.BaseFragment;
import com.vit.myweatherapp.ui.feature.main.MainActivity;

import java.util.List;

import butterknife.BindView;

public class TomorrowFragment extends BaseFragment implements
        MainActivity.OnTomorrowListener,
        HourWeatherAdapter.OnItemClickListener{

    @BindView(R.id.list_weather_tomorrow)
    RecyclerView mRcvWeather;

    private HourWeatherAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tomorrow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setTomorrowListener(this);
    }

    private void initRcvWeather() {
        mRcvWeather.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRcvWeather.setHasFixedSize(true);
        mRcvWeather.setItemAnimator(null);
        mRcvWeather.setAdapter(mAdapter);
    }

    @Override
    public void onPassTomorrowData(List<HourWeatherResponse.Weather_list> hourList) {
        if (hourList != null) {
            mAdapter = new HourWeatherAdapter(hourList, getActivity());
            initRcvWeather();
        }
    }

    @Override
    public void onClick(View view, int position) {

    }
}
