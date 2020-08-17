package moe.lz233.meizugravity.controller.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UriUtil {
    public static String getHostUri(Context context,String port) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting",Context.MODE_PRIVATE);
        return "http://" + sharedPreferences.getString("ip", "") + ":" + port + "/";
    }

    public static String getCmdUri(Context context,String cmd) {
        return getHostUri(context,"2333") + cmd.replace(" ", "%20");
    }
}
