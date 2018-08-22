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
import com.vit.myweatherapp.data.model.HourWeatherResponse;
import com.vit.myweatherapp.data.remote.ApiUtils;
import com.vit.myweatherapp.data.remote.WeatherService;
import com.vit.myweatherapp.ui.adapter.ViewPagerAdapter;
import com.vit.myweatherapp.ui.base.BaseActivity;
import com.vit.myweatherapp.ui.AppConfig;
import com.vit.myweatherapp.ui.feature.map.MapFragment;
import com.vit.myweatherapp.ui.util.Utils;
import com.vit.myweatherapp.ui.widget.TodayView;

import java.util.ArrayList;
import java.util.List;

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
    private FragmentTransaction mFragmentTransaction;

    private OnMainListener mMainListener;
    private OnTodayListener mTodayListener;
    private OnTomorrowListener mTomorrowListener;
    private OnLaterListener mLaterListener;

    private String mInputSearch = "";


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

            setupActionBar();
            initGoogleApi();


            mWeatherService = ApiUtils.getWeatherService();
            ;

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
                mInputSearch = "";
                break;

            case R.id.menu_search:
                showSearchDialog();
                break;

            case R.id.menu_map:
                initMapFragment();
                Timber.i("mInputSearch: " + mInputSearch);
                if (mInputSearch.equals("")) {
                    getCurrentLocation();
                } else {
                    getWeatherSearchFromApi(mInputSearch);
                }
                break;

            case R.id.menu_exit:
                Toast.makeText(this, getString(R.string.menu_exit), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFragmentManager.popBackStack();
    }


    // ---------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ---------------------------------------------------------------------------------------------


    public void setMainListener(OnMainListener listener) {
        this.mMainListener = listener;
    }

    public void setTodayListener(OnTodayListener listener) {
        this.mTodayListener = listener;
    }

    public void setTomorrowListener(OnTomorrowListener listener) {
        this.mTomorrowListener = listener;
    }

    public void setLaterListener(OnLaterListener listener) {
        this.mLaterListener = listener;
    }


    // ---------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ---------------------------------------------------------------------------------------------


    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
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
    private void getCurrentWeatherFromApi(final Location location) {
        mWeatherService.getCurrentWeatherResponse(location.getLatitude(), location.getLongitude(), AppConfig.API_KEY)
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                        if (response.isSuccessful()) {
                            Timber.i("CurrentWeather: " + response.body().getName());
                            mViewToday.setDataForView(getApplicationContext(), response.body());
                            setTitleActionBar(response.body());
                            Timber.i("locationaaa " + location.toString());
                            if (mMainListener != null) {
                                mMainListener.onCurrentWeather(location, response.body());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {

                    }
                });

        mWeatherService.getHourWeatherRespone(location.getLatitude(), location.getLongitude(), AppConfig.API_KEY)
                .enqueue(new Callback<HourWeatherResponse>() {
                    @Override
                    public void onResponse(Call<HourWeatherResponse> call, Response<HourWeatherResponse> response) {
                        List<List<HourWeatherResponse.Weather_list>> hourList = splitDataByDate(response.body().getWeather_list());

                        mTodayListener.onPassTodayData(hourList.get(0));
                        mTomorrowListener.onPassTomorrowData(hourList.get(1));
                        mLaterListener.onPassLaterData(hourList.get(2));


                    }

                    @Override
                    public void onFailure(Call<HourWeatherResponse> call, Throwable t) {

                    }
                });
    }

    /**
     * get city's weather from api
     *
     * @param keyword city's name
     */
    private void getWeatherSearchFromApi(String keyword) {
        mWeatherService.getCurrentWeatherResponse(keyword, AppConfig.API_KEY)
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                        mViewToday.setDataForView(getApplicationContext(), response.body());
                        setTitleActionBar(response.body());
                        if (mMainListener != null) {
                            mMainListener.onCurrentWeather(null, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {

                    }
                });

        mWeatherService.getHourWeatherRespone(keyword, AppConfig.API_KEY)
                .enqueue(new Callback<HourWeatherResponse>() {
                    @Override
                    public void onResponse(Call<HourWeatherResponse> call, Response<HourWeatherResponse> response) {
                        Timber.i("SearchHourAPI: " + response.body().getCity().getName());
                        List<List<HourWeatherResponse.Weather_list>> hourList = splitDataByDate(response.body().getWeather_list());

                        mTodayListener.onPassTodayData(hourList.get(0));
                        mTomorrowListener.onPassTomorrowData(hourList.get(1));
                        mLaterListener.onPassLaterData(hourList.get(2));

                    }

                    @Override
                    public void onFailure(Call<HourWeatherResponse> call, Throwable t) {
                        Timber.e("onFailure: SearchHourAPI " + t.toString());
                    }
                });

    }

    /**
     * split data for date: today, tomorrow, later
     *
     * @param list from api
     */
    private List<List<HourWeatherResponse.Weather_list>> splitDataByDate(List<HourWeatherResponse.Weather_list> list) {
        List<List<HourWeatherResponse.Weather_list>> mHourList = new ArrayList<>();
        try {
            for (int i = 0; i < 3; i++) {
                mHourList.add(new ArrayList<HourWeatherResponse.Weather_list>());
            }

            int i = 0;
            for (HourWeatherResponse.Weather_list l : list) {
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
        return mHourList;
    }

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
                    mInputSearch = inputSearch.getText().toString();
                    getWeatherSearchFromApi(inputSearch.getText().toString());
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


    private void initMapFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.layout_fragment, new MapFragment());
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }


    public interface OnMainListener {
        void onCurrentWeather(Location currentLocation, CurrentWeatherResponse currentWeather);
    }

    public interface OnTodayListener {
        void onPassTodayData(List<HourWeatherResponse.Weather_list> hourList);
    }

    public interface OnTomorrowListener {
        void onPassTomorrowData(List<HourWeatherResponse.Weather_list> hourList);
    }

    public interface OnLaterListener {
        void onPassLaterData(List<HourWeatherResponse.Weather_list> hourList);
    }

}
