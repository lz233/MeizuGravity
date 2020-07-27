package moe.lz233.meizugravity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import moe.lz233.meizugravity.services.DashBoardService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //context.startService(new Intent(context, KeyEventService.class));
            Log.i("ONRECEIVER", "BOOTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
            //Toast.makeText(context,"boot",Toast.LENGTH_LONG).show();
            context.startService(new Intent(context, DashBoardService.class));
            //Runtime.getRuntime().exec("")
            //AppUtil.runRootCommand("pm install -r /sdcard/A8Speaker.apk && cp /sdcard/A8Speaker2.apk /data/app/com.meizu.speaker-1.apk");
        }
    }
}