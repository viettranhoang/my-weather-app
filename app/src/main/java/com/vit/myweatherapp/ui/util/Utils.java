package com.vit.myweatherapp.ui.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vit.myweatherapp.data.remote.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getDate(Integer timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(timestamp * 1000L);
    }

    public static String getHhMmDate(Integer timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(timestamp * 1000L);
    }

    public static String getHhDate(Integer timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        return formatter.format(timestamp * 1000L);
    }



    public static String getCurrentDate() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String getTempCelcius(Double temp) {
        return String.format("%.2f ℃", temp - 273);
    }

    public static void getImageUrl(Context context, ImageView imageView, String idImage) {
        Glide.with(context).load(ApiUtils.ICON_URL + idImage + ".png").into(imageView);
    }

    public static String getImageUrl(String idImage) {
        return ApiUtils.ICON_URL + idImage + ".png";
    }
}
