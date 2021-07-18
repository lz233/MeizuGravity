package moe.lz233.meizugravity.cloudmusic.ui.playing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.bumptech.glide.Glide
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import com.zhy.mediaplayer_exo.playermanager.manager.MediaTrackChangeListener
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityPlayingBinding
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam

class PlayingActivity : BaseActivity() {
    private val viewBuilding by lazy { ActivityPlayingBinding.inflate(layoutInflater) }
    private val mediaTrackChangeListener: MediaTrackChangeListener by lazy {
        {
            Glide.with(viewBuilding.coverImageView)
                    .load(it.coverUrl?.adjustParam("150", "150"))
                    .into(viewBuilding.coverImageView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        Glide.with(viewBuilding.coverImageView)
                .load(MediaManager.getCurrentMediaCover().adjustParam("150", "150"))
                .into(viewBuilding.coverImageView)
        MediaManager.addMediaSwitchChange(mediaTrackChangeListener)
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

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, PlayingActivity::class.java))
    }
}