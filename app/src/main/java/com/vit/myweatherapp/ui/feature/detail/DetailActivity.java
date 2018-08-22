package com.vit.myweatherapp.ui.feature.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.model.HourWeatherResponse;
import com.vit.myweatherapp.ui.base.BaseActivity;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.text_detail)
    TextView mTextDetail;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initView() {
        super.initView();

        String weather =  getIntent().getStringExtra("item");
        mTextDetail.setText(weather);
    }
}
