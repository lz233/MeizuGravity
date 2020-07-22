package io.github.lz233.meizugravity.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.lz233.meizugravity.R;
import io.github.lz233.meizugravity.utils.GetUtil;
import io.github.lz233.meizugravity.view.ChanTextView;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetUtil().sendGet("https://v1.hitokoto.cn",null, new GetUtil.GetCallback() {
                    @Override
                    public void onGetDone(final String result) {
                        hitokotoTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    hitokotoTextView.setText(new JSONObject(result).getString("hitokoto"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }).start();
        return rootView;
    }
}