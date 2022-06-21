package moe.lz233.meizugravity.cloudmusic.utils

import android.content.Context
import android.media.AudioManager
import moe.lz233.meizugravity.design.App.Companion.context

object AudioManager {
    const val VOLUME_UP = AudioManager.ADJUST_RAISE
    const val VOLUME_DOWN = AudioManager.ADJUST_LOWER

    private val audioManager by lazy { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    val maxVolume
        get() = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val currentVolume
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun adjustStreamVolume(direction: Int) = audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, 0)
}