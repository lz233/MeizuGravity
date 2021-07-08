package moe.lz233.meizugravity.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import moe.lz233.meizugravity.BuildConfig;
import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.utils.SystemPropertyUtil;


public abstract class BaseActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    protected View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        rootView = findViewById(android.R.id.content);
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //检测设备
        if (!(SystemPropertyUtil.getSystemProperty("ro.udisk.lable").equals("A8")| BuildConfig.DEBUG)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.unsupportDeviceSummary);
            AlertDialog dialogs = builder.create();
            dialogs.setOnDismissListener(dialog -> finish());
            dialogs.show();
        }
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
        //Intent intent2 = new Intent("moe.lz233.meizugravity.stopkeyevent");
        //intent2.setPackage(getPackageName());
        //sendBroadcast(intent2);
        //Log.i("BaseActivity","onResume");
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
