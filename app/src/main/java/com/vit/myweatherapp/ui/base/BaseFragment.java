package com.vit.myweatherapp.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.remote.ApiUtils;
import com.vit.myweatherapp.ui.adapter.HourWeatherAdapter;
import com.vit.myweatherapp.ui.feature.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public abstract class BaseFragment extends Fragment{

    public BaseFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.i(this.getClass().getSimpleName() + " start");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.i(this.getClass().getSimpleName() + " stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i(this.getClass().getSimpleName() + " destroyed");
    }

}
