package com.vit.myweatherapp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vit.myweatherapp.R;

import butterknife.ButterKnife;
import timber.log.Timber;

public class TodayView extends LinearLayout {

    public TodayView(Context context) {
        super(context);
        initialize();
    }

    public TodayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TodayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        try {
            inflate(getContext(), R.layout.view_today   , this);
            ButterKnife.bind(this);


        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
