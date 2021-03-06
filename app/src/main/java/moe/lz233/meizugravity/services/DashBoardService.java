package moe.lz233.meizugravity.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import moe.lz233.meizugravity.activity.DashBoardActivity;

public class DashBoardService extends Service {
    private SharedPreferences sharedPreferences;

    public DashBoardService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        if (sharedPreferences.getBoolean("autoSleep",true)) {
            new Thread(() -> {
                while (true) {
                    try {
                        //Thread.sleep(10000);
                        Thread.sleep(sharedPreferences.getInt("overTime",300000));
                        if (!sharedPreferences.getBoolean("isInDashBoard", false)) {
                            Intent intent = new Intent(DashBoardService.this, DashBoardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
