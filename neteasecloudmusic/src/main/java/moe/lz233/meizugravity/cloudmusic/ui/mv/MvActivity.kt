package moe.lz233.meizugravity.cloudmusic.ui.mv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityMvBinding
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity

class MvActivity : BaseActivity() {
    private val viewBuilding by lazy { ActivityMvBinding.inflate(layoutInflater) }
    private val player by lazy { SimpleExoPlayer.Builder(this).build() }
    private val shouldPause by lazy { MediaManager.isPlaying() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        if (shouldPause) MediaManager.pause()
        viewBuilding.playerView.player = player
        val mvId = intent.getLongExtra("mvId", 0)
        launch {
            val mvDetailResponse = CloudMusicNetwork.getMvDetail(mvId)
            val mvUrlResponse = CloudMusicNetwork.getMvUrl(mvId, mvDetailResponse.data.resolutionData[1].resolution)
            player.setMediaItem(MediaItem.fromUri(mvUrlResponse.data.url))
            player.prepare()
            player.repeatMode = Player.REPEAT_MODE_ONE
            player.play()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
            KeyEvent.KEYCODE_DPAD_UP -> player.seekTo(player.currentPosition + 15000)
            KeyEvent.KEYCODE_DPAD_DOWN -> player.seekTo(player.currentPosition - 15000)
            KeyEvent.KEYCODE_ENTER -> if (player.isPlaying) player.pause() else player.play()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        if (shouldPause) MediaManager.play()
    }

    companion object {
        fun actionStart(context: Context, mvId: Long) = context.startActivity(Intent(context, MvActivity::class.java).apply { putExtra("mvId", mvId) })
    }
}