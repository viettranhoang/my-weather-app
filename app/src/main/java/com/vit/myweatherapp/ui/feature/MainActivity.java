package com.vit.myweatherapp.ui.feature;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.vit.myweatherapp.ui.base.BaseActivity;
import com.vit.myweatherapp.ui.AppConfig;
import com.vit.myweatherapp.ui.util.Utils;
import com.vit.myweatherapp.ui.widget.DailyView;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    // ---------------------------------------------------------------------------------------------
    // FIELDS
    // ---------------------------------------------------------------------------------------------

    private GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderClient mFusedLocationClient;

    private WeatherService mWeatherService;

    private DailyWeatherResponse mDailyWeather;


    // ---------------------------------------------------------------------------------------------
    // BIND VIEWS
    // ---------------------------------------------------------------------------------------------

    @BindView(R.id.text_city)
    TextView textCity;

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.text_today_main)
    TextView textTodayMain;

    @BindView(R.id.text_today_temp)
    TextView textTodayTemp;

    @BindView(R.id.image_today)
    ImageView imageToday;

    @BindView(R.id.view_daily_one)
    DailyView viewDailyOne;

    @BindView(R.id.view_daily_two)
    DailyView viewDailytwo;

    @BindView(R.id.view_daily_three)
    DailyView viewDailythree;

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

        switch (item.getItemId())
        {
            case R.id.menu_refresh:
                getCurrentLocation();
                break;
            case R.id.menu_search:
                showSearchDialog();
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

    /**
     * setup actionbar
     */
    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hello");

    }

    /**
     * get current weather from api
     *
     * @param location current position
     */
    private void getCurrentWeatherFromApi(Location location) {
        try {
            mWeatherService.getCurrentWeatherResponse(location.getLatitude(), location.getLongitude(), AppConfig.API_KEY)
                    .enqueue(new Callback<CurrentWeatherResponse>() {
                        @Override
                        public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                            if (response.isSuccessful()) {
                                Timber.i("CurrentWeather: " + response.body().getName());
                                setInfoView(response.body());
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
                                setInfoDaily(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            Timber.e(e);
        }
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

    /**
     * get current location
     */
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


    private void setInfoDaily(DailyWeatherResponse d) {
        setDailyView(d, viewDailyOne, 0);
        setDailyView(d, viewDailytwo, 1);
        setDailyView(d, viewDailythree, 2);
    }

    private void setDailyView(DailyWeatherResponse d, DailyView dailyView, int idDay) {
        dailyView.setView("Day +" + (idDay + 1),
                d.getList().get(idDay).getWeather().get(0).getIcon(),
                Utils.getTempMinMax(d.getList().get(idDay).getTemp().getMin(), d.getList().get(idDay).getTemp().getMax()));
    }

    /**
     * set info for views
     *
     * @param c obiect response
     */
    private void setInfoView(CurrentWeatherResponse c) {
        try {
            textCity.setText(c.getName());
            textTime.setText(Utils.getDate(c.getDt()));
            textTodayMain.setText("Today: " + c.getWeather().get(0).getMain());
            textTodayTemp.setText(Utils.getTempMinMax(c.getMain().getTempMin(), c.getMain().getTempMax()));
            Utils.getImageUrl(this, imageToday, c.getWeather().get(0).getIcon());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * show search dialog
     */
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
                            setInfoView(response.body());
                        }

                        @Override
                        public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {

                        }
                    });

            mWeatherService.getDailyWeatherRespone(keyword, AppConfig.API_KEY)
                    .enqueue(new Callback<DailyWeatherResponse>() {
                        @Override
                        public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                            setInfoDaily(response.body());
                        }

                        @Override
                        public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            Timber.e(e);
        }
    }


}
