package com.vit.myweatherapp.ui.feature.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.CurrentWeatherResponse;
import com.vit.myweatherapp.data.model.DailyWeatherResponse;
import com.vit.myweatherapp.data.remote.ApiUtils;
import com.vit.myweatherapp.data.remote.WeatherService;
import com.vit.myweatherapp.ui.adapter.ViewPagerAdapter;
import com.vit.myweatherapp.ui.base.BaseActivity;
import com.vit.myweatherapp.ui.AppConfig;
import com.vit.myweatherapp.ui.widget.TodayView;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // ---------------------------------------------------------------------------------------------
    // BIND VIEWS
    // ---------------------------------------------------------------------------------------------

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.layout_tab)
    TabLayout mLayoutTab;

    @BindView(R.id.view_today)
    TodayView mViewToday;



    // ---------------------------------------------------------------------------------------------
    // FIELDS
    // ---------------------------------------------------------------------------------------------

    private GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderClient mFusedLocationClient;

    private WeatherService mWeatherService;

    private FragmentManager mFragmentManager;


    // ---------------------------------------------------------------------------------------------
    // OVERIDE METHODS
    // ---------------------------------------------------------------------------------------------

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();

        try {
            setupActionBar();
            initGoogleApi();

            mFragmentManager = getSupportFragmentManager();
            mWeatherService = ApiUtils.getWeatherService();
        } catch (Exception e) {
            Timber.e(e);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.error_connect + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.menu_refresh:
                getCurrentLocation();
                mViewToday.setLastUpdate();
                break;
            case R.id.menu_search:
                showSearchDialog();
                break;
            case R.id.menu_map:
                addFragmentMap();
                break;
            case R.id.menu_exit:
                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }


    // ---------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ---------------------------------------------------------------------------------------------


    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mLayoutTab.setupWithViewPager(mViewPager);

    }

    private void setTitleActionBar(CurrentWeatherResponse c) {
        getSupportActionBar().setTitle(c.getName());
    }

    /**
     * get current weather from api
     *
     * @param location current position
     */
    private void getCurrentWeatherFromApi(Location location) {
        mWeatherService.getCurrentWeatherResponse(location.getLatitude(), location.getLongitude(), AppConfig.API_KEY)
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                        if (response.isSuccessful()) {
                            Timber.i("CurrentWeather: " + response.body().getName());
                            mViewToday.setDataForView(getApplicationContext(), response.body());
                            setTitleActionBar(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {

                    }
                });

        mWeatherService.getDailyWeatherRespone(location.getLatitude(), location.getLongitude(), 4, AppConfig.API_KEY)
                .enqueue(new Callback<DailyWeatherResponse>() {
                    @Override
                    public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                        if (response.isSuccessful()) {
                            Timber.i("DailyWeather: " + response.body().getCity().getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {

                    }
                });
    }

    /**
     * setup google service
     */
    private void initGoogleApi() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void getCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            } else {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    getCurrentWeatherFromApi(location);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void showSearchDialog() {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_search, null);
            dialogBuilder.setView(dialogView);

            final EditText inputSearch = dialogView.findViewById(R.id.input_search);
            dialogBuilder.setTitle(R.string.message_title_search);
            dialogBuilder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    getDataSearchFromApi(inputSearch.getText().toString());
                    dialog.cancel();
                }
            });
            dialogBuilder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * get city's weather from api
     *
     * @param keyword city's name
     */
    private void getDataSearchFromApi(String keyword) {
        try {
            mWeatherService.getCurrentWeatherResponse(keyword, AppConfig.API_KEY)
                    .enqueue(new Callback<CurrentWeatherResponse>() {
                        @Override
                        public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                            mViewToday.setDataForView(getApplicationContext(), response.body());
                            setTitleActionBar(response.body());
                        }

                        @Override
                        public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {

                        }
                    });

            mWeatherService.getDailyWeatherRespone(keyword, AppConfig.API_KEY)
                    .enqueue(new Callback<DailyWeatherResponse>() {
                        @Override
                        public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                        }

                        @Override
                        public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void addFragmentMap() {
        mFragmentManager.popBackStackImmediate();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.layout_fragment, new MapFragment());
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFragmentManager.popBackStack();
    }
}
