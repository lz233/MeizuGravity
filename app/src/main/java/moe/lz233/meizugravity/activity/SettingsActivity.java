package moe.lz233.meizugravity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.utils.FileUtil;
import moe.lz233.meizugravity.utils.ToastUtil;
import moe.lz233.meizugravity.utils.ViewPager2Util;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //
        new SettingTCPServer().listen();
        //

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
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }
    class SettingTCPServer {
        private static final int port = 2333;

        public void listen() {
            new Thread(() -> {
                try{
                    ServerSocket serverSocket = new ServerSocket(port);
                    while (true) {
                        Socket socket = serverSocket.accept();
                        new Thread(() -> {
                            try {
                                String ip = socket.getInetAddress().getHostAddress();
                                InputStream inputStream = socket.getInputStream();
                                OutputStream outputStream = socket.getOutputStream();
                                //读取请求数据
                                byte[] bytes = new byte[inputStream.available()];
                                inputStream.read(bytes);
                                String str = new String(bytes, StandardCharsets.UTF_8);
                                Log.d("SettingService",str);
                                //Response response = new Response.Builder().
                                String flags = str.substring(str.indexOf("GET") + 5, str.indexOf(" HTTP"));
                                String key = flags.substring(0,flags.indexOf("="));
                                String value = flags.substring(flags.indexOf("=")+1);
                                switch (key){
                                    case "autoSleep":
                                        editor.putBoolean("autoSleep",Boolean.parseBoolean(value));
                                        editor.apply();
                                        break;
                                    case "overTime":
                                        editor.putInt("overTime",Integer.parseInt(value));
                                        editor.apply();
                                        break;
                                    case "brightness":
                                        editor.putFloat("brightness",Float.parseFloat(value));
                                        editor.apply();
                                        break;
                                    case "location":
                                        editor.putString("location",value);
                                        editor.apply();
                                        break;
                                    case "coolapkUid":
                                        editor.putString("coolapkUid",value);
                                        editor.apply();
                                        break;
                                }
                                Looper.prepare();
                                ToastUtil.showShort(SettingsActivity.this,flags);
                                outputStream.write(addHeader("ok").getBytes());
                                Log.d("CommandService", "----------------------------------------\n" + str + "\n----------------------------------------");
                                inputStream.close();
                                outputStream.close();
                                socket.close();
                                Looper.loop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }
    }
    private String addHeader(String string) {
        String header = "HTTP/1.1 200 OK\nContent-Type: text/plain; charset=UTF-8\n\n";
        return header + string;
    }
}