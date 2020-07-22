package moe.lz233.meizugravity.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.fragment.CoolapkFragment;
import moe.lz233.meizugravity.fragment.WeatherFragment;
import moe.lz233.meizugravity.utils.SettingUtil;
import moe.lz233.meizugravity.utils.ViewPager2Util;

public class DashBoardActivity extends BaseActivity {
    int itemCount = 2;
    private SettingUtil settingUtil;
    private Thread brightThread;
    private ViewPager2 dashViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        //
        dashViewPager2 = findViewById(R.id.dashViewPager2);
        //
        settingUtil = new SettingUtil(this);
        setScreenBrightnessValue((float) settingUtil.getDoublt("brightness"));
        //brightThread = new Thread(new Run());
        //brightThread.start();
        editor.putBoolean("isInDashBoard", true);
        editor.apply();
        dashViewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        dashViewPager2.setUserInputEnabled(false);
        //dashViewPager2.setOffscreenPageLimit(2);
        dashViewPager2.setAdapter(new ScreenSlidePagerAdapter(this));
    }

    @Override
    protected void onPause() {
        editor.putBoolean("isInDashBoard", false);
        editor.apply();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            int targetItem = dashViewPager2.getCurrentItem() - 1;
            if (targetItem < 0) {
                targetItem = 0;
            }
            ViewPager2Util.setCurrentItem(dashViewPager2, targetItem, 100, 0);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            int targetItem = dashViewPager2.getCurrentItem() + 1;
            if (targetItem >= itemCount) {
                targetItem = dashViewPager2.getCurrentItem();
            }
            ViewPager2Util.setCurrentItem(dashViewPager2, targetItem, 100, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    private float getScreenBrightnessValue() {
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        return localLayoutParams.screenBrightness;
    }

    /**
     * 设置应用的亮度，看代码就知道只是某个界面(窗口)亮度
     *
     * @param brightnessValue 0.0f - 1.0f
     */
    private void setScreenBrightnessValue(float brightnessValue) {
        final Window localWindow = getWindow();
        final WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = brightnessValue;
        dashViewPager2.post(new Runnable() {
            @Override
            public void run() {
                localWindow.setAttributes(localLayoutParams);
            }
        });
    }

    private class Run implements Runnable {
        @Override
        public void run() {
            boolean lessBright = true;
            //setScreenBrightnessValue(0.5f);
            while (true) {
                if (lessBright) {
                    //setScreenBrightnessValue(0.1f);
                    //lessBright = false;
                    setScreenBrightnessValue(getScreenBrightnessValue() - 0.1f);
                    if (getScreenBrightnessValue() <= 0.1f) {
                        lessBright = false;
                    }
                } else {
                    //setScreenBrightnessValue(0.5f);
                    //lessBright = true;
                    setScreenBrightnessValue(getScreenBrightnessValue() + 0.1f);
                    if (getScreenBrightnessValue() >= 0.5f) {
                        lessBright = true;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                default:
                    return new WeatherFragment(settingUtil);
                case 1:
                    return new CoolapkFragment(settingUtil);
            }
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }
    }
}