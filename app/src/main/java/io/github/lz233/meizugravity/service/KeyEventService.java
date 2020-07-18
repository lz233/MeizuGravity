package io.github.lz233.meizugravity.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import io.github.lz233.meizugravity.activity.MainActivity;

public class KeyEventService extends AccessibilityService {
    @Override
    protected void onServiceConnected() {
        Log.i("connected","233");
        //AccessibilityServiceInfo info = getServiceInfo();
        //info.packageNames = new String[]{"com.android.settings"};
        //this.setServiceInfo(info);
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.i("key", String.valueOf(event.getKeyCode()));
        SharedPreferences sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        if (event.getKeyCode()==KeyEvent.KEYCODE_MENU){
            if (!sharedPreferences.getBoolean("appIsOn",false)){
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        return false;
    }
}
