package moe.lz233.meizugravity.cloudmusic.utils.ktx

import android.content.Context
import android.media.AudioManager
import moe.lz233.meizugravity.cloudmusic.App

object AudioManager {
    const val VOLUME_UP = AudioManager.ADJUST_RAISE
    const val VOLUME_DOWN = AudioManager.ADJUST_LOWER

    val audioManager by lazy { App.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    val maxVolume
        get() = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val currentVolume
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun adjustStreamVolume(direction: Int) = audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, 0)
}