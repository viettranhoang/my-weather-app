package com.vit.myweatherapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vit.myweatherapp.R;
import com.vit.myweatherapp.data.remote.ApiUtils;
import com.vit.myweatherapp.ui.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DailyView extends LinearLayout{

    // ---------------------------------------------------------------------------------------------
    // FIELDS
    // ---------------------------------------------------------------------------------------------



    // ---------------------------------------------------------------------------------------------
    // BIND VIES
    // ---------------------------------------------------------------------------------------------

    @BindView(R.id.text_day_title)
    TextView textDayTitle;

    @BindView(R.id.image_day)
    ImageView imageDay;

    @BindView(R.id.text_day_temp)
    TextView textDayTemp;

    // ---------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ---------------------------------------------------------------------------------------------

    public DailyView(Context context) {
        super(context);
        initialize();
    }

    public DailyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DailyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setView(String mTitleDay, String mImageDay, String mTempDay) {
        textDayTitle.setText(mTitleDay);
        textDayTemp.setText(mTempDay);
        Utils.getImageUrl(getContext(), imageDay, mImageDay);
    }


    // ---------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ---------------------------------------------------------------------------------------------

    private void initialize() {
        try {
            inflate(getContext(), R.layout.view_daily, this);
            ButterKnife.bind(this);


        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
