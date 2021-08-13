package com.zhy.mediaplayer_exo.playermanager.musicbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.zhy.mediaplayer_exo.playermanager.MediaPlayerService
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager

class MusicBroadcast : BroadcastReceiver() {
    companion object {
        @JvmStatic
        val PENDINGINTENT_NO_READY_PLAY_CLICK =
                "com.simplemusic.musicbroadcast.action.noready.play.click"

        @JvmStatic
        val ACTION_MUSIC_BROADCASET_UPDATE = "com.simplemusic.musicbroadcast.update.action_._"

        @JvmStatic
        val EXTRA_ACTION = "com.simplemusic.musicbroadcast.data.action"

        @JvmStatic
        val PENDINGINTENT_PLAY_CLICK = "com.simplemusic.musicbroadcast.action.play.click"

        @JvmStatic
        val PENDINGINTENT_LAST_MUSIC_CLICK = "com.simplemusic.musicbroadcast.action.lastmusic.click"

        @JvmStatic
        val PENDINGINTENT_LAST_NEXT_MUSIC_CLICK =
                "com.simplemusic.musicbroadcast.action.nextmusic.click"

        @JvmStatic
        val PENDINGINTENT_READY_PLAY_CLICK =
                "com.simplemusic.musicbroadcast.action.ready.play.click"

        @JvmStatic
        val PENDINGINTENT_CLOSE_MUSIC_SERVICE =
                "com.simplemusic.musicbroadcast.action.close.play..service.click"
    }

    private val mediaProgressRunnable = MediaProgressRunnable()
    val mHandler = Handler(Looper.getMainLooper())
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == ACTION_MUSIC_BROADCASET_UPDATE) {
                val data = it.getStringExtra(EXTRA_ACTION)
                when (data) {
                    PENDINGINTENT_PLAY_CLICK -> {
                        if (MediaManager.getSimpleExoPlayer().isPlaying) {
                            MediaManager.getSimpleExoPlayer().pause()
                            mHandler.removeCallbacks(mediaProgressRunnable)
                        } else {
                            MediaManager.getSimpleExoPlayer().play()
                            mHandler.postDelayed(mediaProgressRunnable, 300)
                        }
                    }
                    PENDINGINTENT_LAST_MUSIC_CLICK -> MediaManager.playLast()
                    PENDINGINTENT_LAST_NEXT_MUSIC_CLICK -> MediaManager.playNext()
                    PENDINGINTENT_READY_PLAY_CLICK -> {
                        mHandler.removeCallbacks(mediaProgressRunnable)
                        mHandler.postDelayed(mediaProgressRunnable, 300)

                    }
                    PENDINGINTENT_NO_READY_PLAY_CLICK -> {
                    }
                    PENDINGINTENT_CLOSE_MUSIC_SERVICE -> MediaPlayerService.stop(context!!)
                }
            }
        }
    }

    inner class MediaProgressRunnable : Runnable {
        override fun run() {
            MediaManager.invokeProgressListenerList(MediaManager.getSimpleExoPlayer().currentPosition)
            mHandler.postDelayed(this, 300)
        }
    }
}