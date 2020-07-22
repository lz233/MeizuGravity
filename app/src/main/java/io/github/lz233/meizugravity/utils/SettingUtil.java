package io.github.lz233.meizugravity.utils;

import android.content.Context;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingUtil {
    JSONObject jsonObject = new JSONObject();

    public SettingUtil(@Nullable Context context) {
        try {
            if (!FileUtil.isFile("/sdcard/Android/data/io.github.lz233.meizugravity/files/settings.json")){
                initSetting(context);
            }
            jsonObject = new JSONObject(FileUtil.readTextFromFile("/sdcard/Android/data/io.github.lz233.meizugravity/files/settings.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initSetting(Context context){
        if (context!=null){
            FileUtil.copyAssets(context, "files","/sdcard/Android/data/io.github.lz233.meizugravity/files");
        }
    }
    public String getString(String key) {
        return jsonObject.optString(key);
    }

    public int getInt(String key) {
        return jsonObject.optInt(key);
    }

    public long getLong(String key) {
        return jsonObject.optLong(key);
    }

    public boolean getBoolean(String key) {
        return jsonObject.optBoolean(key);
    }

    public double getDoublt(String key) {
        return jsonObject.optDouble(key);
    }
}
