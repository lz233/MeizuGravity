package moe.lz233.meizugravity.controller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import moe.lz233.meizugravity.controller.BuildConfig;
import moe.lz233.meizugravity.controller.R;
import moe.lz233.meizugravity.controller.fragment.GravityFragment;
import moe.lz233.meizugravity.controller.fragment.KeyEventFragment;
import moe.lz233.meizugravity.controller.fragment.TouchPadFragment;
import moe.lz233.meizugravity.controller.service.AdbService;
import moe.lz233.meizugravity.controller.util.FileUtil;
import moe.lz233.meizugravity.controller.util.libadb.AdbConnection;
import moe.lz233.meizugravity.controller.util.libadb.AdbCrypto;
import moe.lz233.meizugravity.controller.util.libadb.AdbStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static moe.lz233.meizugravity.controller.util.UriUtil.getHostUri;

public class MainActivity extends BaseActivity {
    private final OkHttpClient client = new OkHttpClient();

    private String ip;
    private static final int NUM_PAGES = 3;
    private MediaType json = MediaType.parse("application/json; charset=utf-8");

    private ViewPager2 viewPager2;
    private FragmentStateAdapter fragmentStateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        viewPager2 = findViewById(R.id.viewPager2);
        fragmentStateAdapter = new ScreenSlidePagerAdapter(this);
        //
        setSupportActionBar(toolbar);
        ip = sharedPreferences.getString("ip", "");
        if (ip.equals("")) {
            showSettings();
        } else {
            startService(new Intent(this, AdbService.class));
        }
        viewPager2.setAdapter(fragmentStateAdapter);
        viewPager2.setOffscreenPageLimit(2);
        //
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings:
                showSettings();
                break;
            case R.id.actionOpenCloudMusic:
                startService(new Intent(this, AdbService.class).putExtra("cmd", "am start com.netease.cloudmusic/.activity.LoadingActivity"));
                break;
            case R.id.actionDeviceInfo:
                client.newCall(new Request.Builder().get().url(getHostUri(MainActivity.this,"7766")+"Info").build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                            builder.setTitle(R.string.actionDeviceInfo);
                            LayoutInflater inflater = getLayoutInflater();
                            final View view = inflater.inflate(R.layout.dialog_device_info, null);
                            TextView deviceNameTextView = view.findViewById(R.id.deviceNameTextView);
                            TextView deviceIDTextView = view.findViewById(R.id.deviceIDTextView);
                            TextView deviceVerTextView = view.findViewById(R.id.deviceVerTextView);
                            TextView deviceVersionCodeTextView = view.findViewById(R.id.deviceVersionCodeTextView);
                            TextView deviceBspVerTextView = view.findViewById(R.id.deviceBspVerTextView);
                            TextView deviceBootVerTextView =view.findViewById(R.id.deviceBootVerTextView);
                            TextView deviceKernelVerTextView = view.findViewById(R.id.deviceKernelVerTextView);
                            JSONObject jsonObject = new JSONObject(response.body().string()).getJSONObject("data");
                            deviceNameTextView.post(() -> {
                                deviceNameTextView.setText(jsonObject.optString("deviceName"));
                                deviceIDTextView.setText(jsonObject.optString("deviceID"));
                                deviceVerTextView.setText(jsonObject.optString("deviceVersion"));
                                deviceVersionCodeTextView.setText(jsonObject.optString("deviceVersionCode"));
                                deviceBspVerTextView.setText(jsonObject.optString("deviceBSPVer"));
                                deviceBootVerTextView.setText(jsonObject.optString("deviceBootVer"));
                                deviceKernelVerTextView.setText(jsonObject.optString("deviceKernelVer"));
                            });
                            builder.setView(view);
                            builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            });
                            viewPager2.post(() -> {
                                AlertDialog materialDialogs = builder.create();
                                materialDialogs.show();
                                materialDialogs.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    return new GravityFragment(sharedPreferences,editor);
                case 1:
                    return new KeyEventFragment(sharedPreferences,editor);
                case 2:
                    return new TouchPadFragment(viewPager2,sharedPreferences,editor);
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
    private void showSettings() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle(R.string.actionSettings);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_settings, null);
        TextInputEditText ipTextInputEditText = view.findViewById(R.id.ipTextInputEditText);
        ipTextInputEditText.setText(sharedPreferences.getString("ip", ""));
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            String mIp = ipTextInputEditText.getText().toString();
            if (mIp.equals("")) {
                Toast.makeText(this, R.string.notAllowedToBeEmpty, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            } else {
                ip = mIp;
                editor.putString("ip", mIp);
                editor.apply();
                dialogInterface.dismiss();
                stopService(new Intent(this, AdbService.class));
                startService(new Intent(this, AdbService.class));
            }
        });
        builder.setNegativeButton(R.string.cancael, (dialogInterface, i) -> {
            String mIp = ipTextInputEditText.getText().toString();
            if (mIp.equals("")) {
                Toast.makeText(this, R.string.notAllowedToBeEmpty, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            } else {
                dialogInterface.dismiss();
            }
        });
        AlertDialog materialDialogs = builder.create();
        materialDialogs.setCancelable(false);
        materialDialogs.show();
        materialDialogs.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        materialDialogs.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
