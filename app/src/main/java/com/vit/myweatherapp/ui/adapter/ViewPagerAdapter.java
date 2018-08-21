package com.vit.myweatherapp.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vit.myweatherapp.R;
import com.vit.myweatherapp.ui.feature.main.fragment.LaterFragment;
import com.vit.myweatherapp.ui.feature.main.fragment.TodayFragment;
import com.vit.myweatherapp.ui.feature.main.fragment.TomorrowFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    int tabTitles[] = new int[] {R.string.date_today, R.string.date_tomorrow, R.string.date_later};

    private Context mContext;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TodayFragment();
            case 1:
                return new TomorrowFragment();
            case 2:
                return new LaterFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  mContext.getString(tabTitles[position]);
    }
}
