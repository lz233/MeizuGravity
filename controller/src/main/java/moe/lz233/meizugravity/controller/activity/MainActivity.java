package moe.lz233.meizugravity.controller.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.IOException;
import java.io.OutputStream;

import moe.lz233.meizugravity.controller.BuildConfig;
import moe.lz233.meizugravity.controller.R;
import moe.lz233.meizugravity.controller.fragment.KeyEventFragment;
import moe.lz233.meizugravity.controller.util.FileUtil;
import okhttp3.MediaType;

public class MainActivity extends BaseActivity {
    private String ip;
    private static final int NUM_PAGES = 1;
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
            /*new GetUtil().sendGet("http://" + ip + ":7766/Info", result -> {
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                        nameTextView.setText(jsonObject.optString("deviceName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/
        }
        viewPager2.setAdapter(fragmentStateAdapter);
        viewPager2.setOffscreenPageLimit(2);
        //
        /*nameTextView.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
            builder.setTitle(R.string.name);
            LayoutInflater inflater = getLayoutInflater();
            final View view1 = inflater.inflate(R.layout.dialog_rename, null);
            TextInputEditText nameTextInputEditText = view1.findViewById(R.id.nameTextInputEditText);
            nameTextInputEditText.setText(nameTextView.getText());
            builder.setView(view1);
            builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                String mName = nameTextInputEditText.getText().toString();
                if (mName.equals("")) {
                    Toast.makeText(MainActivity.this, R.string.notAllowedToBeEmpty, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        RequestBody requestBody = RequestBody.create(new JSONObject().put("deviceName",mName).toString(),json);
                        Request request = new Request.Builder().url("http://"+ip+":7766/Rename").post(requestBody).build();
                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                nameTextView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        nameTextView.setText(mName);
                                    }
                                });
                                dialogInterface.dismiss();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancael, (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog materialDialogs = builder.create();
            materialDialogs.setCancelable(false);
            materialDialogs.show();
            materialDialogs.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            materialDialogs.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettings();
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
                    return new KeyEventFragment(sharedPreferences,editor);
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
    private void showSettings() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle(R.string.action_settings);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_settings, null);
        TextInputEditText ipTextInputEditText = view.findViewById(R.id.ipTextInputEditText);
        ipTextInputEditText.setText(sharedPreferences.getString("ip", ""));
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            String mIp = ipTextInputEditText.getText().toString();
            if (mIp.equals("")) {
                Toast.makeText(this, R.string.notAllowedToBeEmpty, Toast.LENGTH_SHORT).show();
            } else {
                ip = mIp;
                editor.putString("ip", mIp);
                editor.apply();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancael, (dialogInterface, i) -> {
            String mIp = ipTextInputEditText.getText().toString();
            if (mIp.equals("")) {
                Toast.makeText(this, R.string.notAllowedToBeEmpty, Toast.LENGTH_SHORT).show();
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
