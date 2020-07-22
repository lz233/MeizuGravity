package moe.lz233.meizugravity.utils;

import android.content.Context;

import moe.lz233.meizugravity.R;

public class WeatherUtil {
    public static String getAQIDescription(Context context, String region, double AQIValute) {
        String result = "";
        if (region.equals("chn")) {
            if (AQIValute > 250) {
                result = context.getString(R.string.AQI6);
            } else if (AQIValute > 150) {
                result = context.getString(R.string.AQI5);
            } else if (AQIValute > 115) {
                result = context.getString(R.string.AQI4);
            } else if (AQIValute > 75) {
                result = context.getString(R.string.AQI3);
            } else if (AQIValute > 35) {
                result = context.getString(R.string.AQI2);
            } else {
                result = context.getString(R.string.AQI1);
            }
        } else if (region.equals("usa")) {
            if (AQIValute > 250.4) {
                result = context.getString(R.string.AQI6);
            } else if (AQIValute > 150.4) {
                result = context.getString(R.string.AQI5);
            } else if (AQIValute > 55.4) {
                result = context.getString(R.string.AQI4);
            } else if (AQIValute > 35.4) {
                result = context.getString(R.string.AQI3);
            } else if (AQIValute > 12) {
                result = context.getString(R.string.AQI2);
            } else {
                result = context.getString(R.string.AQI1);
            }
        }
        return result;
    }

    public static String getSkycon(Context context, String skyconValue) {
        String result = "";
        switch (skyconValue) {
            case "CLEAR_DAY":
            case "CLEAR_NIGHT":
                result = context.getString(R.string.sunny);
                break;
            case "PARTLY_CLOUDY_DAY":
            case "PARTLY_CLOUDY_NIGHT":
                result = context.getString(R.string.partlyCloudy);
                break;
            case "CLOUDY":
                result = context.getString(R.string.cloudy);
                break;
            case "LIGHT_HAZE":
                result = context.getString(R.string.lightHaze);
                break;
            case "MODERATE_HAZE":
                result = context.getString(R.string.moderateHaze);
                break;
            case "HEAVY_HAZE":
                result = context.getString(R.string.heavyHaze);
                break;
            case "LIGHT_RAIN":
                result = context.getString(R.string.lightRain);
                break;
            case "MODERATE_RAIN":
                result = context.getString(R.string.moderateRain);
                break;
            case "HEAVY_RAIN":
                result = context.getString(R.string.heavyRain);
                break;
            case "STORM_RAIN":
                result = context.getString(R.string.stormRain);
                break;
            case "FOG":
                result = context.getString(R.string.fog);
                break;
            case "LIGHT_SNOW":
                result = context.getString(R.string.lightSnow);
                break;
            case "MODERATE_SNOW":
                result = context.getString(R.string.moderateSnow);
                break;
            case "HEAVY_SNOW":
                result = context.getString(R.string.heavySnow);
                break;
            case "STORM_SNOW":
                result = context.getString(R.string.stormSnow);
                break;
            case "DUST":
                result = context.getString(R.string.dust);
                break;
            case "SAND":
                result = context.getString(R.string.sand);
                break;
            case "WIND":
                result = context.getString(R.string.wind);
                break;
        }
        return result;
    }
}
