package moe.lz233.meizugravity.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.utils.GetUtil;
import moe.lz233.meizugravity.view.ChanTextView;

public class ClockFragment extends Fragment {
    private LinearLayout clockLinearLayout;
    private ChanTextView hitokotoTextView;

    public ClockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_clock, container, false);
        //
        clockLinearLayout = rootView.findViewById(R.id.clockLinearLayout);
        hitokotoTextView = rootView.findViewById(R.id.hitokotoTextView);
        //
        clockLinearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        new Thread(() -> new GetUtil().sendGet("https://netease-cloud-music-api-kvzk9t0k3-lz233.vercel.app/", null, result ->{})).start();
        new Thread(() -> new GetUtil().sendGet("https://v1.hitokoto.cn", null, result ->
                hitokotoTextView.post(() -> {
                    try {
                        hitokotoTextView.setText(new JSONObject(result).getString("hitokoto"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }))).start();
        return rootView;
    }
}