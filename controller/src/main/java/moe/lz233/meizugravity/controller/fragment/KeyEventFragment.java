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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

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
    private ExtendedFloatingActionButton runCommandExtendedFloatingActionButton;
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
        runCommandExtendedFloatingActionButton = rootView.findViewById(R.id.runCommandExtendedFloatingActionButton);
        volumeDownExtendedFloatingActionButton = rootView.findViewById(R.id.volumeDownExtendedFloatingActionButton);
        volumeUpExtendedFloatingActionButton = rootView.findViewById(R.id.volumeUpExtendedFloatingActionButton);
        backFloatingActionButton = rootView.findViewById(R.id.backFloatingActionButton);
        homeFloatingActionButton = rootView.findViewById(R.id.homeFloatingActionButton);
        menuFloatingActionButton = rootView.findViewById(R.id.menuFloatingActionButton);
        //
        new Thread(() -> {
            try {
                sock = connectSocket(sharedPreferences.getString("ip",""),7788);
                crypto = AdbCrypto.setupCrypto("pub.key","priv.key",getContext());
                adb = AdbConnection.create(sock,crypto);
                adb.connect();
                stream = adb.open("shell:");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        //
        upFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 19\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        leftFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 21\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        centerFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 66\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        rightFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 22\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        downFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 20\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        runCommandExtendedFloatingActionButton.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle(R.string.actionRunCommand);
            final View view1 = inflater.inflate(R.layout.dialog_run_command, null);
            TextInputEditText commandTextInputEditText = view1.findViewById(R.id.commandTextInputEditText);
            commandTextInputEditText.setText("input text ");
            builder.setView(view1);
            builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                new Thread(() -> {
                    try {
                        stream.write(commandTextInputEditText.getText().toString()+"\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            });
            builder.setNegativeButton(R.string.cancael, (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            AlertDialog materialDialogs = builder.create();
            materialDialogs.setCancelable(true);
            materialDialogs.show();
            materialDialogs.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            materialDialogs.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        });
        volumeDownExtendedFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 25\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        volumeUpExtendedFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 24\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        backFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 4\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        homeFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 3\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        menuFloatingActionButton.setOnClickListener(view -> new Thread(() -> {
            try {
                stream.write("input keyevent 82\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());
        return rootView;
    }
}
