package moe.lz233.meizugravity.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

import moe.lz233.meizugravity.meta.AppInfo;

public class AppsInfoUtil {

    private PackageManager packageManager;
    private List<AppInfo> appInfos = new ArrayList<AppInfo>();

    public AppsInfoUtil(Context mContext) {
        ActivityManager activityManager =
                (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        packageManager = mContext.getPackageManager();
    }

    private void loadAppsInfo() {
        List<ResolveInfo> apps = null;
        Intent filterIntent = new Intent(Intent.ACTION_MAIN, null);
        //Intent.CATEGORY_LAUNCHER主要的过滤条件
        filterIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        apps = packageManager.queryIntentActivities(filterIntent, 0);
        for (ResolveInfo resolveInfo : apps) {
            AppInfo appInfo = new AppInfo();
            appInfo.setPackageName(resolveInfo.activityInfo.applicationInfo.packageName);
            appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
            appInfos.add(appInfo);
        }
    }

    //外部获取信息的方法
    public List<AppInfo> getAppList() {
        loadAppsInfo();
        return appInfos;
    }
}