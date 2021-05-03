package moe.lz233.meizugravity.controller.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.Socket;

import moe.lz233.meizugravity.controller.R;
import moe.lz233.meizugravity.controller.service.AdbService;
import okhttp3.MediaType;

public class BaseFragment extends Fragment {
    protected MediaType json = MediaType.parse("application/json; charset=utf-8");
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    public BaseFragment() {
    }

    public BaseFragment(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    public static Socket connectSocket(String ip, int port) throws IOException {
        // Connect the socket to the remote host
        return new Socket(ip, port);
    }

    public void showRunCommandDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle(R.string.actionRunCommand);
        final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_run_command, null);
        TextInputEditText commandTextInputEditText = view1.findViewById(R.id.commandTextInputEditText);
        commandTextInputEditText.setText(sharedPreferences.getString("command", "input text "));
        builder.setView(view1);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            editor.putString("command", commandTextInputEditText.getText().toString());
            editor.apply();
            getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", commandTextInputEditText.getText().toString()));
        });
        builder.setNegativeButton(R.string.cancael, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog materialDialogs = builder.create();
        materialDialogs.setCancelable(true);
        materialDialogs.show();
        materialDialogs.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        materialDialogs.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }

}
