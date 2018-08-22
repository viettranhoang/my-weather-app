package com.vit.myweatherapp.ui.feature.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.CurrentWeatherResponse;
import com.vit.myweatherapp.ui.base.BaseFragment;
import com.vit.myweatherapp.ui.feature.main.MainActivity;
import com.vit.myweatherapp.ui.util.Utils;

import butterknife.BindView;

public class MapFragment extends BaseFragment implements OnMapReadyCallback,
        MainActivity.OnMainListener {

    @BindView(R.id.view_map)
    MapView mViewMap;

    private GoogleMap mGoogleMap;

    private Marker mMarker;

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

        ((MainActivity) getActivity()).setMainListener(this);

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


    @Override
    public void onCurrentWeather(Location currentLocation, CurrentWeatherResponse currentWeather) {
        String url = Utils.getImageUrl(currentWeather.getWeather().get(0).getIcon());
        String weather = currentWeather.getWeather().get(0).getDescription();

        if (currentLocation != null) {
            showImageWeather(currentLocation.getLatitude(), currentLocation.getLongitude(), url, weather);
        } else {
            showImageWeather(currentWeather.getCoord().getLat(), currentWeather.getCoord().getLon(), url, weather);
        }
    }


    private void showImageWeather(Double lat, Double lon, String url, final String title) {
        final LatLng position = new LatLng(lat, lon);

        Glide.with(this).asBitmap()
                .load(url)
                .apply(new RequestOptions().override(100, 100))
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                mMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(resource)));

                showCameraToPosition(position);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                mGoogleMap.addMarker(new MarkerOptions().position(position));
                showCameraToPosition(position);
            }
        });
    }


    public void showCameraToPosition(LatLng position) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(position)
                .zoom(15f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
        }

    }

}
