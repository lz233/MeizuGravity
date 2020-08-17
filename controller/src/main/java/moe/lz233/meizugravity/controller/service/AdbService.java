package moe.lz233.meizugravity.controller.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.io.IOException;
import java.net.Socket;

import moe.lz233.meizugravity.controller.util.libadb.AdbConnection;
import moe.lz233.meizugravity.controller.util.libadb.AdbCrypto;
import moe.lz233.meizugravity.controller.util.libadb.AdbStream;

public class AdbService extends Service {
    private static AdbStream stream;
    private static AdbConnection adb;
    private static Socket sock = null;
    private static AdbCrypto crypto;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AdbService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        new Thread(() -> {
            try {
                sock = connectSocket(sharedPreferences.getString("ip", ""), 7788);
                crypto = AdbCrypto.setupCrypto("pub.key", "priv.key", this);
                adb = AdbConnection.create(sock, crypto);
                adb.connect();
                stream = adb.open("shell:");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String cmd = intent.getStringExtra("cmd");
        if (cmd != null) {
            new Thread(() -> {
                try {
                    stream.write(cmd + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread(() -> {
            try {
                stream.close();
                adb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Socket connectSocket(String ip, int port) throws IOException {
        // Connect the socket to the remote host
        return new Socket(ip, port);
    }
}
