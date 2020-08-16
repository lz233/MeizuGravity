package moe.lz233.meizugravity.controller.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import moe.lz233.meizugravity.controller.BuildConfig;
import moe.lz233.meizugravity.controller.R;
import moe.lz233.meizugravity.controller.activity.MainActivity;
import moe.lz233.meizugravity.controller.util.FileUtil;
import moe.lz233.meizugravity.controller.util.GetUtil;
import moe.lz233.meizugravity.controller.util.libadb.AdbConnection;
import moe.lz233.meizugravity.controller.util.libadb.AdbCrypto;
import moe.lz233.meizugravity.controller.util.libadb.AdbStream;

public class KeyEventFragment extends BaseFragment {
    private static AdbStream stream;
    private static AdbConnection adb;
    private static Socket sock = null;
    private static AdbCrypto crypto;

    private FloatingActionButton upFloatingActionButton;
    private FloatingActionButton leftFloatingActionButton;
    private FloatingActionButton centerFloatingActionButton;
    private FloatingActionButton rightFloatingActionButton;
    private FloatingActionButton downFloatingActionButton;
    private ExtendedFloatingActionButton volumeDownExtendedFloatingActionButton;
    private ExtendedFloatingActionButton volumeUpExtendedFloatingActionButton;
    private FloatingActionButton backFloatingActionButton;
    private FloatingActionButton homeFloatingActionButton;
    private FloatingActionButton menuFloatingActionButton;
    public KeyEventFragment(SharedPreferences sharedPreferences, SharedPreferences.Editor editor){
        super(sharedPreferences, editor);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_keyevent,container,false);
        //
        upFloatingActionButton = rootView.findViewById(R.id.upFloatingActionButton);
        leftFloatingActionButton = rootView.findViewById(R.id.leftFloatingActionButton);
        centerFloatingActionButton = rootView.findViewById(R.id.centerFloatingActionButton);
        rightFloatingActionButton = rootView.findViewById(R.id.rightFloatingActionButton);
        downFloatingActionButton = rootView.findViewById(R.id.downFloatingActionButton);
        volumeDownExtendedFloatingActionButton = rootView.findViewById(R.id.volumeDownExtendedFloatingActionButton);
        volumeUpExtendedFloatingActionButton = rootView.findViewById(R.id.volumeUpExtendedFloatingActionButton);
        backFloatingActionButton = rootView.findViewById(R.id.backFloatingActionButton);
        homeFloatingActionButton = rootView.findViewById(R.id.homeFloatingActionButton);
        menuFloatingActionButton = rootView.findViewById(R.id.menuFloatingActionButton);
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sock = connectSocket("192.168.43.194",7788);
                    crypto = AdbCrypto.setupCrypto("pub.key","priv.key",getActivity());
                    adb = AdbConnection.create(sock,crypto);
                    adb.connect();
                    stream = adb.open("shell:");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //
        upFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 19"), result -> {

        }));
        leftFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 21"), result -> {

        }));
        centerFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 66"), result -> {

        }));
        centerFloatingActionButton.setOnLongClickListener(view -> {
            new GetUtil().sendGet(getCmdUri("input keyevent --longpress 66"), result -> {

            });
            return true;
        });
        rightFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 22"), result -> {

        }));
        /*downFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 20"), result -> {

        }));*/
        downFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stream.write("input keyevent 4\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        volumeDownExtendedFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 25"), result -> {

        }));
        volumeUpExtendedFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 24"), result -> {

        }));
        backFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 4"), result -> {

        }));
        homeFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 3"), result -> {

        }));
        menuFloatingActionButton.setOnClickListener(view -> new GetUtil().sendGet(getCmdUri("input keyevent 82"), result -> {

        }));
        return rootView;
    }
}
