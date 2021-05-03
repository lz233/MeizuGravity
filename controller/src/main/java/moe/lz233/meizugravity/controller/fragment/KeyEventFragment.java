package moe.lz233.meizugravity.controller.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import moe.lz233.meizugravity.controller.R;
import moe.lz233.meizugravity.controller.service.AdbService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static moe.lz233.meizugravity.controller.util.UriUtil.getHostUri;

public class KeyEventFragment extends BaseFragment {
    private final OkHttpClient client = new OkHttpClient();

    private FloatingActionButton upFloatingActionButton;
    private FloatingActionButton leftFloatingActionButton;
    private FloatingActionButton centerFloatingActionButton;
    private FloatingActionButton rightFloatingActionButton;
    private FloatingActionButton downFloatingActionButton;
    private ExtendedFloatingActionButton runCommandExtendedFloatingActionButton;
    private IndicatorSeekBar volumeIndicatorSeekBar;
    private FloatingActionButton backFloatingActionButton;
    private FloatingActionButton playFloatingActionButton;
    private FloatingActionButton powerFloatingActionButton;

    public KeyEventFragment() {
    }

    public KeyEventFragment(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        super(sharedPreferences, editor);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_keyevent, container, false);
        //
        upFloatingActionButton = rootView.findViewById(R.id.upFloatingActionButton);
        leftFloatingActionButton = rootView.findViewById(R.id.leftFloatingActionButton);
        centerFloatingActionButton = rootView.findViewById(R.id.centerFloatingActionButton);
        rightFloatingActionButton = rootView.findViewById(R.id.rightFloatingActionButton);
        downFloatingActionButton = rootView.findViewById(R.id.downFloatingActionButton);
        runCommandExtendedFloatingActionButton = rootView.findViewById(R.id.runCommandExtendedFloatingActionButton);
        volumeIndicatorSeekBar = rootView.findViewById(R.id.volumeIndicatorSeekBar);
        backFloatingActionButton = rootView.findViewById(R.id.backFloatingActionButton);
        playFloatingActionButton = rootView.findViewById(R.id.playFloatingActionButton);
        powerFloatingActionButton = rootView.findViewById(R.id.powerFloatingActionButton);
        //

        //
        upFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 19")));
        leftFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 21")));
        centerFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 66")));
        rightFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 22")));
        downFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 20")));
        runCommandExtendedFloatingActionButton.setOnClickListener(view -> {
            showRunCommandDialog();
        });
        volumeIndicatorSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if (seekBar.getProgress() > 25) {
                    showVolumeDialog(seekBar.getProgress());
                } else {
                    setVolume(seekBar.getProgress());
                }
            }
        });
        backFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 4")));
        playFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 85")));
        powerFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 26")));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getVolume();
    }

    private void showVolumeDialog(int volume) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle(R.string.volumeDialogTitle);
        builder.setMessage(R.string.volumeDialogMessage);
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> setVolume(volume));
        builder.setNegativeButton(R.string.cancael, ((dialog, which) -> getVolume()));
        AlertDialog materialDialogs = builder.create();
        materialDialogs.show();
        materialDialogs.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        materialDialogs.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void setVolume(int volume) {
        try {
            RequestBody requestBody = RequestBody.create(new JSONObject().put("CurrentVolume", volume).toString(), json);
            Request request = new Request.Builder().post(requestBody).url(getHostUri(getActivity(), "7766") + "SetVolume").build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getVolume() {
        client.newCall(new Request.Builder().get().url(getHostUri(getActivity(), "7766") + "Info").build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string()).getJSONObject("data");
                    volumeIndicatorSeekBar.post(() -> volumeIndicatorSeekBar.setProgress(jsonObject.optInt("currentVolume")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
