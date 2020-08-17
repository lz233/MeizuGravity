package moe.lz233.meizugravity.controller.fragment;

import android.content.SharedPreferences;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.Socket;

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
}
