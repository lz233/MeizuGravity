package io.github.lz233.meizugravity.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.io.IOException;

import io.github.lz233.meizugravity.activity.MainActivity;

public class MonitorView extends View {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String LOG_TAG = "MonitorView";
    public MonitorView(Context context) {
        super(context);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting",Context.MODE_PRIVATE);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //Log.i(LOG_TAG, "KeyEvent.KEYCODE_BACK");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.i(LOG_TAG, "KeyEvent.KEYCODE_MENU");
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                //Log.i(LOG_TAG, "音量减小");
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                //Log.i(LOG_TAG, "音量增大");
                break;
            default:
                break;
        }
        return false;
    }
}
