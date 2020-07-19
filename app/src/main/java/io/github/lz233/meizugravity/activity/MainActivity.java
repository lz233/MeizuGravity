package io.github.lz233.meizugravity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.github.lz233.meizugravity.R;
import io.github.lz233.meizugravity.utils.AppUtil;
import io.github.lz233.meizugravity.utils.ToastUtils;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume() {
        ToastUtils.showShort(this,"test");
        editor.putBoolean("appIsOn", true);
        editor.apply();
        super.onResume();
    }

    @Override
    protected void onPause() {
        editor.putBoolean("appIsOn", false);
        editor.apply();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("MainActivity", String.valueOf(keyCode));
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            finish();
        }else if (keyCode == KeyEvent.KEYCODE_ENTER){

        }
        return false;
    }
}