package com.vit.myweatherapp.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vit.myweatherapp.ui.feature.main.HourWeatherFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 2:
                return new HourWeatherFragment();
            case 0:
                return new HourWeatherFragment();
            case 1:
                return new HourWeatherFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TODAY";
            case 1:
                return "TOMORROW";
            case 2:
                return "LATER";
            default:
                return null;
        }

    }
}
