package io.github.lz233.meizugravity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import io.github.lz233.meizugravity.R;
import io.github.lz233.meizugravity.service.KeyEventService;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        //AppUtil.runRootCommand("settings put secure enabled_accessibility_services io.github.lz233.meizugravity/io.github.lz233.meizugravity.service.KeyEventService && settings put secure accessibility_enabled 1");
        startService(new Intent(this, KeyEventService.class));
        //
    }
    @Override
    protected void onResume() {
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
        }
        return false;
    }
}