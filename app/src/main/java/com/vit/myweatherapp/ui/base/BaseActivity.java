package com.vit.myweatherapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vit.myweatherapp.BuildConfig;
import com.vit.myweatherapp.R;
import com.vit.myweatherapp.logger.CrashReportingTree;

import butterknife.ButterKnife;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindViews();
        initTimber();
        initView();
        initFont();
    }

    /**
     * Every object annotated with {@link ButterKnife #bind(View)} its gonna injected trough butterknife
     */
    private void bindViews() {
        ButterKnife.bind(this);
    }

    /**
     * Use this method to initialize view components. This method is called after {@link
     * BaseActivity#bindViews()}
     */
    public void initView() {
    }

    /**
     * @return The layout id that's gonna be the activity view.
     */
    protected abstract int getLayoutId();

    /**
     * setup Timber
     */
    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
