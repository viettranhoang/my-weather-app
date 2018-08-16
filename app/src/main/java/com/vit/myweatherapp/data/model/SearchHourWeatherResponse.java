package com.vit.myweatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchHourWeatherResponse {


    @Expose
    @SerializedName("city")
    private City city;
    @Expose
    @SerializedName("list")
    private List<Weather_list> weather_list;
    @Expose
    @SerializedName("cnt")
    private int cnt;
    @Expose
    @SerializedName("message")
    private double message;
    @Expose
    @SerializedName("cod")
    private String cod;

    public City getCity() {
        return city;
    }

    public List<Weather_list> getWeather_list() {
        return weather_list;
    }

    public int getCnt() {
        return cnt;
    }

    public double getMessage() {
        return message;
    }

    public String getCod() {
        return cod;
    }

    public static class City {
        @Expose
        @SerializedName("country")
        private String country;
        @Expose
        @SerializedName("coord")
        private Coord coord;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

        public String getCountry() {
            return country;
        }

        public Coord getCoord() {
            return coord;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    public static class Coord {
        @Expose
        @SerializedName("lon")
        private double lon;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }
    }

    public static class Weather_list {
        @Expose
        @SerializedName("dt_txt")
        private String dt_txt;
        @Expose
        @SerializedName("sys")
        private Sys sys;
        @Expose
        @SerializedName("snow")
        private Snow snow;
        @Expose
        @SerializedName("rain")
        private Rain rain;
        @Expose
        @SerializedName("wind")
        private Wind wind;
        @Expose
        @SerializedName("clouds")
        private Clouds clouds;
        @Expose
        @SerializedName("weather")
        private List<Weather> weather;
        @Expose
        @SerializedName("main")
        private Main main;
        @Expose
        @SerializedName("dt")
        private int dt;

        public String getDt_txt() {
            return dt_txt;
        }

        public Sys getSys() {
            return sys;
        }

        public Snow getSnow() {
            return snow;
        }

        public Rain getRain() {
            return rain;
        }

        public Wind getWind() {
            return wind;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public Main getMain() {
            return main;
        }

        public int getDt() {
            return dt;
        }
    }

    public static class Sys {
        @Expose
        @SerializedName("pod")
        private String pod;

        public String getPod() {
            return pod;
        }
    }

    public static class Snow {
        @Expose
        @SerializedName("3h")
        private double three_h;

        public double getThree_h() {
            return three_h;
        }
    }

    public static class Rain {
        @Expose
        @SerializedName("3h")
        private double three_h;

        public double getThree_h() {
            return three_h;
        }
    }

    public static class Wind {
        @Expose
        @SerializedName("deg")
        private double deg;
        @Expose
        @SerializedName("speed")
        private double speed;

        public double getDeg() {
            return deg;
        }

        public double getSpeed() {
            return speed;
        }
    }

    public static class Clouds {
        @Expose
        @SerializedName("all")
        private int all;

        public int getAll() {
            return all;
        }
    }

    public static class Weather {
        @Expose
        @SerializedName("icon")
        private String icon;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("main")
        private String main;
        @Expose
        @SerializedName("id")
        private int id;

        public String getIcon() {
            return icon;
        }

        public String getDescription() {
            return description;
        }

        public String getMain() {
            return main;
        }

        public int getId() {
            return id;
        }
    }

    public static class Main {
        @Expose
        @SerializedName("temp_kf")
        private double temp_kf;
        @Expose
        @SerializedName("humidity")
        private int humidity;
        @Expose
        @SerializedName("grnd_level")
        private double grnd_level;
        @Expose
        @SerializedName("sea_level")
        private double sea_level;
        @Expose
        @SerializedName("pressure")
        private double pressure;
        @Expose
        @SerializedName("temp_max")
        private double temp_max;
        @Expose
        @SerializedName("temp_min")
        private double temp_min;
        @Expose
        @SerializedName("temp")
        private double temp;

        public double getTemp_kf() {
            return temp_kf;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getGrnd_level() {
            return grnd_level;
        }

        public double getSea_level() {
            return sea_level;
        }

        public double getPressure() {
            return pressure;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public double getTemp() {
            return temp;
        }
    }
}

