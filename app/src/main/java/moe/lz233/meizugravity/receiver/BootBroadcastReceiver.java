package moe.lz233.meizugravity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import moe.lz233.meizugravity.services.CommandService;
import moe.lz233.meizugravity.services.DashBoardService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //context.startService(new Intent(context, KeyEventService.class));
            Log.i("ONRECEIVER", "BOOTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
            //Toast.makeText(context,"ACTION_BOOT_COMPLETED",Toast.LENGTH_LONG).show();
            context.startService(new Intent(context, DashBoardService.class));
            //context.startService(new Intent(context, CommandService.class));
            //Runtime.getRuntime().exec("")
            //AppUtil.runRootCommand("pm install -r /sdcard/A8Speaker.apk && cp /sdcard/A8Speaker2.apk /data/app/com.meizu.speaker-1.apk");
        }
    }
}
