package com.vit.myweatherapp.ui.feature.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.vit.myweatherapp.R;
import com.vit.myweatherapp.ui.base.BaseFragment;

import butterknife.BindView;

public class MapFragment extends BaseFragment implements OnMapReadyCallback{

    @BindView(R.id.view_map)
    MapView mViewMap;

    private GoogleMap mGoogleMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mViewMap != null) {
            mViewMap.onCreate(savedInstanceState);
            mViewMap.onResume();
            mViewMap.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;


    }

    @Override
    public void onResume() {
        super.onResume();
        mViewMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mViewMap.onLowMemory();
    }


}
