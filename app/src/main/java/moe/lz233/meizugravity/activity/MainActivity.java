package moe.lz233.meizugravity.activity;

import static moe.lz233.meizugravity.App.editor;
import static moe.lz233.meizugravity.App.sp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.design.activity.BaseActivity;
import moe.lz233.meizugravity.design.utils.LogUtil;
import moe.lz233.meizugravity.design.utils.ViewPager2Util;
import moe.lz233.meizugravity.utils.GetUtil;

public class MainActivity extends BaseActivity {
    private final ArrayList<String> mainMenuTitleList = new ArrayList<>();
    private final ArrayList<Integer> mainMenuIconList = new ArrayList<>();

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
        //startService(new Intent(this, CommandService.class));
        if (sp.getBoolean("isFirstRun", true)) {
            LogUtil.toast(getString(R.string.firstTips), Toast.LENGTH_LONG);
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }
        new GetUtil().sendGet("http://127.0.0.1:7766/Status", null, result -> {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (!jsonObject.optJSONObject("data").optJSONObject("playList").optJSONArray("trackList").optJSONObject(0).optString("cp").equals("local")) {
                    startActivity(new Intent().setClass(MainActivity.this, LrcActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        mainMenuTitleList.add(getString(R.string.dashboard));
        mainMenuIconList.add(R.drawable.ic_dashboard);
        mainMenuTitleList.add(getString(R.string.apps));
        mainMenuIconList.add(R.drawable.ic_apps);
        mainMenuTitleList.add(getString(R.string.setting));
        mainMenuIconList.add(R.drawable.ic_settings);
        mainMenuTitleList.add(getString(R.string.about));
        mainMenuIconList.add(R.drawable.ic_info);
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
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Log.i("MainActivity", String.valueOf(keyCode));
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            int targetItem = mainViewPager2.getCurrentItem() - 1;
            if (targetItem < 0) {
                targetItem = 0;
            }
            ViewPager2Util.setCurrentItem(mainViewPager2, targetItem, 100, 50);
            mainImageView.setImageResource(mainMenuIconList.get(targetItem));
            //mainViewPager2.setCurrentItem(mainViewPager2.getCurrentItem()-1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            int targetItem = mainViewPager2.getCurrentItem() + 1;
            if (targetItem >= mainMenuTitleList.size()) {
                targetItem = mainViewPager2.getCurrentItem();
            }
            ViewPager2Util.setCurrentItem(mainViewPager2, targetItem, 100, 50);
            mainImageView.setImageResource(mainMenuIconList.get(targetItem));
            //mainViewPager2.setCurrentItem(mainViewPager2.getCurrentItem()+1);
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            switch (mainViewPager2.getCurrentItem()) {
                case 0:
                    startActivity(new Intent().setClass(this, DashBoardActivity.class));
                    break;
                case 1:
                    LauncherActivity.Companion.actionStart(this);
                    break;
                case 2:
                    startActivity(new Intent().setClass(this, SettingsActivity.class));
                    break;
                case 3:
                    AboutActivity.Companion.actionStart(this);
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.CardViewHolder> {

        @NonNull
        @Override
        public ViewPagerAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerAdapter.CardViewHolder holder, int position) {
            holder.textView.setText(mainMenuTitleList.get(position));
        }

        @Override
        public int getItemCount() {
            return mainMenuTitleList.size();
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