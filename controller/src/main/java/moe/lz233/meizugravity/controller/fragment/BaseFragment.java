package moe.lz233.meizugravity.controller.fragment;

import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.Socket;

public class BaseFragment extends Fragment {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    public BaseFragment(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    public String getHostUri(String port) {
        return "http://" + sharedPreferences.getString("ip", "") + ":" + port + "/";
    }

    public String getCmdUri(String cmd) {
        return getHostUri("2333") + cmd.replace(" ", "%20");
    }

    public static Socket connectSocket(String ip, int port) throws IOException {
        // Connect the socket to the remote host
        return new Socket(ip, port);
    }
}
