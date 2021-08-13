package com.zhy.mediaplayer_exo.playermanager.service

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import com.zhy.mediaplayer_exo.playermanager.musicbroadcast.MusicBroadcast


class MediaForegroundService : Service() {
    companion object {
        @JvmStatic
        val EXTRA_NOTIFICATION_DATA = "EXTRA_NOTIFICATION_DATA"

        @JvmStatic
        val musicBroadcast = MusicBroadcast()

        @JvmStatic
        var startService = 0

        /**
         * 判断某个服务是否正在运行的方法
         *
         * @param mContext
         * @param serviceName
         * 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
         * @return true代表正在运行，false代表服务没有正在运行
         */
        fun isServiceWork(
                mContext: Context,
                serviceName: String
        ): Boolean {
            var isWork = false
            val myAM = mContext
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val myList: List<ActivityManager.RunningServiceInfo> = myAM.getRunningServices(40)
            if (myList.isEmpty()) {
                return false
            }
            for (i in myList.indices) {
                val mName: String = myList[i].service.getClassName().toString()
                if (mName == serviceName) {
                    isWork = true
                    break
                }
            }
            return isWork
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            startService++
            registerReceiver(musicBroadcast, IntentFilter(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE))
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MBinder()
    }

    inner class MBinder : Binder() {

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        //移除通知栏
        stopForeground(true)
        stopSelf()
        unregisterReceiver(musicBroadcast)
        MediaManager.getSimpleExoPlayer().pause()
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        println("MediaForegroundService onDestroy")
//        android.os.Process.killProcess(android.os.Process.myPid())
        super.onDestroy()
    }


}