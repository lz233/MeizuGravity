package io.github.lz233.meizugravity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.lz233.meizugravity.service.KeyEventService;

public class BaseActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
}
