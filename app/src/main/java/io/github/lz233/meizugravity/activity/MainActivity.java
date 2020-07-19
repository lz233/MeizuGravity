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

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        //AppUtil.runRootCommand("settings put secure enabled_accessibility_services io.github.lz233.meizugravity/io.github.lz233.meizugravity.service.KeyEventService && settings put secure accessibility_enabled 1");
        //startService(new Intent(this, KeyEventService.class));
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
        }else if (keyCode == KeyEvent.KEYCODE_ENTER){
            try {
                //执行命令
                Process p = Runtime.getRuntime().exec("pm install -r /sdcard/A8Speaker.apk && su && cp /sdcard/A8Speaker2.apk /data/app/com.meizu.speaker-1.apk");
                //取得命令结果的输出流
                InputStream fis=p.getInputStream();
                //用一个读输出流类去读
                InputStreamReader isr=new InputStreamReader(fis);
                //用缓冲器读行
                BufferedReader br=new BufferedReader(isr);
                String line=null;
                //直到读完为止
                while((line=br.readLine())!=null)
                {
                   Log.i("SHELL",line);
                }
                //Runtime.getRuntime().exec("pm install -r /sdcard/A8Speaker.apk && su && cp /sdcard/A8Speaker2.apk /data/app/com.meizu.speaker-1.apk");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //AppUtil.runRootCommand("pm install -r /sdcard/A8Speaker.apk && cp /sdcard/A8Speaker2.apk /data/app/com.meizu.speaker-1.apk");
        }
        return false;
    }
}