package moe.lz233.meizugravity.fragment;

import android.animation.LayoutTransition;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.utils.GetUtil;
import moe.lz233.meizugravity.utils.WeatherUtil;
import moe.lz233.meizugravity.view.ChanTextView;

public class WeatherFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LinearLayout weatherLinearLayout;
    private ChanTextView temperatureTextView;
    private ChanTextView skyconTextView;
    private ChanTextView precipitationIntensityTextView;
    private ChanTextView temperatureDay0TextView;
    private ChanTextView skyconDay0TextView;
    private ChanTextView AQIDay0TextView;
    private ChanTextView temperatureDay1TextView;
    private ChanTextView skyconDay1TextView;
    private ChanTextView AQIDay1TextView;

    public WeatherFragment(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_weather, container, false);
        //
        weatherLinearLayout = rootView.findViewById(R.id.weatherLinearLayout);
        temperatureTextView = rootView.findViewById(R.id.temperatureTextView);
        skyconTextView = rootView.findViewById(R.id.skyconTextView);
        precipitationIntensityTextView = rootView.findViewById(R.id.precipitationIntensityTextView);
        temperatureDay0TextView = rootView.findViewById(R.id.temperatureDay0TextView);
        skyconDay0TextView = rootView.findViewById(R.id.skyconDay0TextView);
        AQIDay0TextView = rootView.findViewById(R.id.AQIDay0TextView);
        temperatureDay1TextView = rootView.findViewById(R.id.temperatureDay1TextView);
        skyconDay1TextView = rootView.findViewById(R.id.skyconDay1TextView);
        AQIDay1TextView = rootView.findViewById(R.id.AQIDay1TextView);
        //
        weatherLinearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        new Thread(() -> new GetUtil().sendGet("https://api.caiyunapp.com/v2.5/Y2FpeXVuIGFuZHJpb2QgYXBp/" + sharedPreferences.getString("location","121.549177,31.3189266") + "/weather.json", null, result -> {
            try {
                JSONObject jsonObject = new JSONObject(result).getJSONObject("result");
                final JSONObject realObject = jsonObject.getJSONObject("realtime");
                final JSONObject realAirQualityObject = realObject.getJSONObject("air_quality");
                final JSONObject realPrecipitationObject = realObject.getJSONObject("precipitation");
                JSONObject dailyObject = jsonObject.getJSONObject("daily");
                final JSONArray dailyTemperatureArray = dailyObject.getJSONArray("temperature");
                final JSONArray dailyAirQualityAQIArray = dailyObject.getJSONObject("air_quality").getJSONArray("aqi");
                final JSONArray dailySkyconArray = dailyObject.getJSONArray("skycon");
                temperatureTextView.post(() -> {
                    temperatureTextView.setText(realObject.optString("temperature").substring(0, 2) + "℃");
                    skyconTextView.setText(getString(R.string.AQI) + realAirQualityObject.optJSONObject("aqi").optString("chn") + " (" + realAirQualityObject.optJSONObject("description").optString("chn") + ")  " + WeatherUtil.getSkycon(getContext(), realObject.optString("skycon")));
                    precipitationIntensityTextView.setText(getString(R.string.precipitationIntensity) + realPrecipitationObject.optJSONObject("local").optDouble("intensity") * 100 + "%");
                    temperatureDay0TextView.setText(dailyTemperatureArray.optJSONObject(0).optString("max").substring(0, 2) + "/" + dailyTemperatureArray.optJSONObject(0).optString("min").substring(0, 2));
                    AQIDay0TextView.setText(dailyAirQualityAQIArray.optJSONObject(0).optJSONObject("avg").optString("chn") + " (" + WeatherUtil.getAQIDescription(getContext(), "chn", dailyAirQualityAQIArray.optJSONObject(0).optJSONObject("avg").optDouble("chn")) + ")");
                    skyconDay0TextView.setText(WeatherUtil.getSkycon(getContext(), dailySkyconArray.optJSONObject(0).optString("value")));
                    temperatureDay1TextView.setText(dailyTemperatureArray.optJSONObject(1).optString("max").substring(0, 2) + "/" + dailyTemperatureArray.optJSONObject(1).optString("min").substring(0, 2));
                    AQIDay1TextView.setText(dailyAirQualityAQIArray.optJSONObject(1).optJSONObject("avg").optString("chn") + " (" + WeatherUtil.getAQIDescription(getContext(), "chn", dailyAirQualityAQIArray.optJSONObject(1).optJSONObject("avg").optDouble("chn")) + ")");
                    skyconDay1TextView.setText(WeatherUtil.getSkycon(getContext(), dailySkyconArray.optJSONObject(1).optString("value")));

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
        return rootView;
    }
}