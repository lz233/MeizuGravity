package moe.lz233.meizugravity.fragment;

import android.animation.LayoutTransition;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.utils.CoolapkAuthUtil;
import moe.lz233.meizugravity.utils.GetUtil;
import moe.lz233.meizugravity.view.ChanTextView;

public class CoolapkFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LinearLayout coolapkLinearLayout;
    private ImageView avatarImageView;
    private ChanTextView nameTextView;
    private ChanTextView followerTextView;
    private ChanTextView introductionTextView;

    public CoolapkFragment(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_coolapk, container, false);
        //
        coolapkLinearLayout = rootView.findViewById(R.id.coolapkLinearLayout);
        avatarImageView = rootView.findViewById(R.id.avatarImageView);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        followerTextView = rootView.findViewById(R.id.followerTextView);
        introductionTextView = rootView.findViewById(R.id.introductionTextView);
        //
        coolapkLinearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String[]> prop = new ArrayList<>();
                prop.add(new String[]{"USER_AGENT","Dalvik/2.1.0 (Linux; U; Android 5.1.1; G011A Build/LMY48Z) (#Build; google; G011A; google-user 5.1.1 20171130.276299 release-keys; 5.1.1) +CoolMarket/10.4-2007081"});
                prop.add(new String[]{"X-Sdk-Int", "22"});
                prop.add(new String[]{"X-Sdk-Locale", "zh-CN"});
                prop.add(new String[]{"X-App-Id", "com.coolapk.market"});
                prop.add(new String[]{"X-App-Token", CoolapkAuthUtil.getAS()});
                prop.add(new String[]{"X-App-Version", "10.4"});
                prop.add(new String[]{"X-App-Code", "2007081"});
                prop.add(new String[]{"X-Api-Version", "10"});
                prop.add(new String[]{"X-App-Device", "EUMxAzRgsTZsd2bvdGI7UGbn92bnByOBljO4cjO1UkOBFjO3UjOwgDI7YTNxEDO0AzNzgTNwYjN0AyO0cTN3IDO5ETN5MjN2gDOgsjMhhTYxcTO1MWNzUTZjZGM"});
                prop.add(new String[]{"X-Dark-Mode", "0"});
                prop.add(new String[]{"X-Requested-With", "XMLHttpRequest"});
                new GetUtil().sendGet("https://api.coolapk.com/v6/user/space?uid=" + sharedPreferences.getString("coolapkUid","917649"), prop, result -> {
                    try {
                        final JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                        nameTextView.post(() -> {
                            nameTextView.setText(jsonObject.optString("username") + " LV." + jsonObject.optString("level"));
                            followerTextView.setText(jsonObject.optString("follow") + " " + getString(R.string.following) + "｜" + jsonObject.optString("fans") + " " + getString(R.string.follower));
                            introductionTextView.setText(jsonObject.optString("bio"));
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();
        return rootView;
    }
}