package io.github.lz233.meizugravity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.github.lz233.meizugravity.utils.AppUtil;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //context.startService(new Intent(context, KeyEventService.class));
            Log.i("ONRECEIVER","BOOTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
            Toast.makeText(context,"boot",Toast.LENGTH_LONG).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Process p = Runtime.getRuntime().exec("pm install -r /sdcard/A8Speaker.apk && su && cp /sdcard/A8Speaker2.apk /data/app/com.meizu.speaker-1.apk");
                        //取得命令结果的输出流
                        InputStream fis=p.getInputStream();
                        //用一个读输出流类去读
                        InputStreamReader isr=new InputStreamReader(fis);
                        //用缓冲器读行
                        BufferedReader br=new BufferedReader(isr);
                        String line=null;
                        //直到读完为止
                        while((line=br.readLine())!=null)
                        {
                            Log.i("SHELL",line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //AppUtil.runRootCommand("pm install -r /sdcard/A8Speaker.apk && cp /sdcard/A8Speaker2.apk /data/app/com.meizu.speaker-1.apk");
        }
    }
}
