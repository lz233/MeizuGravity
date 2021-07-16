package moe.lz233.meizugravity.cloudmusic.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.exoplayer2.SimpleExoPlayer

class MusicService : Service() {
    val player by lazy { SimpleExoPlayer.Builder(this).build() }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}