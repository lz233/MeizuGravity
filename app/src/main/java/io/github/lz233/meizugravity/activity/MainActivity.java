package io.github.lz233.meizugravity.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.DialogCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import io.github.lz233.meizugravity.R;
import io.github.lz233.meizugravity.utils.SystemPropertyUtil;
import io.github.lz233.meizugravity.utils.ToastUtils;
import io.github.lz233.meizugravity.utils.ViewPager2Util;

public class MainActivity extends BaseActivity {
    private ArrayList<String> mainMenuList = new ArrayList<>();

    private ImageView mainImageView;
    private ViewPager2 mainViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mainImageView = findViewById(R.id.mainImageView);
        mainViewPager2 = findViewById(R.id.mainViewPager2);
        //
        if (!SystemPropertyUtil.getSystemProperty("ro.udisk.lable").equals("A8")){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.unsupportDeviceSummary);
            AlertDialog dialogs = builder.create();
            dialogs.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            dialogs.show();
        }
        mainMenuList.add(getString(R.string.dashboard));
        mainMenuList.add(getString(R.string.setting));
        mainMenuList.add(getString(R.string.about));
        mainMenuList.add("1111111111111111111111111111111111111111111111111111111111111111");
        mainViewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        mainViewPager2.setUserInputEnabled(false);
        mainViewPager2.setOffscreenPageLimit(3);
        mainViewPager2.setAdapter(new ViewPagerAdapter());
        //一屏多页
        RecyclerView recyclerView = (RecyclerView) mainViewPager2.getChildAt(0);
        recyclerView.setPadding(0, 25, 0, 25);
        recyclerView.setClipToPadding(false);
    }

    @Override
    protected void onResume() {
        ToastUtils.showShort(this, "test");
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
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            ViewPager2Util.setCurrentItem(mainViewPager2, mainViewPager2.getCurrentItem() - 1, 100);
            //mainViewPager2.setCurrentItem(mainViewPager2.getCurrentItem()-1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            ViewPager2Util.setCurrentItem(mainViewPager2, mainViewPager2.getCurrentItem() + 1, 100);
            //mainViewPager2.setCurrentItem(mainViewPager2.getCurrentItem()+1);
        }
        return false;
    }

    public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.CardViewHolder> {

        @NonNull
        @Override
        public ViewPagerAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerAdapter.CardViewHolder holder, int position) {
            holder.textView.setText(mainMenuList.get(position));
        }

        @Override
        public int getItemCount() {
            return mainMenuList.size();
        }

        public class CardViewHolder extends RecyclerView.ViewHolder {

            public TextView textView;

            public CardViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.mainItemTextView);
            }
        }
    }
}