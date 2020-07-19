package io.github.lz233.meizugravity.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.github.lz233.meizugravity.R;


public class BaseActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //全局自定义字体
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("font.otf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }
    @Override
    protected void onResume() {
        //Intent intent2 = new Intent("io.github.lz233.meizugravity.stopkeyevent");
        //intent2.setPackage(getPackageName());
        //sendBroadcast(intent2);
        Log.i("BaseActivity","onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
       //startService(new Intent(this,KeyEventService.class));
        super.onPause();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
