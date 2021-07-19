package moe.lz233.meizugravity.cloudmusic.ui.playing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.bumptech.glide.Glide
import com.zhy.mediaplayer_exo.playermanager.MediaSwitchTrackChange
import com.zhy.mediaplayer_exo.playermanager.PlaylistItem
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityPlayingBinding
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam

class PlayingActivity : BaseActivity() {
    private val viewBuilding by lazy { ActivityPlayingBinding.inflate(layoutInflater) }
    private val isPlaying = MediaManager.isPlaying()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            LogUtil.d(MediaManager.getDuration())
            LogUtil.d(MediaManager.getCurrentPosition())
            viewBuilding.lrcView.updateTime(MediaManager.getCurrentPosition())
            handler.postDelayed(this, 400)
        }
    }

    private val mediaTrackChangeListener = object : MediaSwitchTrackChange {
        override fun onTracksChange(playlistItem: PlaylistItem) {
            onMediaChange(playlistItem)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        if (isPlaying) onMediaChange()
        handler.post(runnable)
        MediaManager.addMediaSwitchChange(mediaTrackChangeListener)
    }

    private fun onMediaChange(playlistItem: PlaylistItem = MediaManager.getCurrentMedia()) {
        Glide.with(viewBuilding.coverImageView)
                .load(playlistItem.coverUrl?.adjustParam("150", "150"))
                .into(viewBuilding.coverImageView)
        launch {
            val songLyricResponse = CloudMusicNetwork.getSongLyric(MediaManager.currentId()!!.toLong())
            if (songLyricResponse.translatedLyric.lyric == "")
                viewBuilding.lrcView.loadLrc(songLyricResponse.lyric.lyric)
            else
                viewBuilding.lrcView.loadLrc(songLyricResponse.lyric.lyric, songLyricResponse.translatedLyric.lyric)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
            KeyEvent.KEYCODE_DPAD_UP -> {
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
            }
            KeyEvent.KEYCODE_ENTER -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        MediaManager.removeMediaSwitchChange(mediaTrackChangeListener)
        super.onDestroy()
    }

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, PlayingActivity::class.java))
    }
}