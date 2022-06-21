package moe.lz233.meizugravity.cloudmusic.ui.time

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.kd.easybarrage.Barrage
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityTimeBinding
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.design.activity.BaseActivity

class TimeActivity : BaseActivity() {
    private val viewBuilding by lazy { ActivityTimeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        setScreenBrightnessValue(0.1f)
        MediaManager.pause()
        viewBuilding.musicNameTextView.text = "▶ ${MediaManager.getCurrentMediaName()}"
        launch {
            val songCommentResponse = CloudMusicNetwork.getSongComment(MediaManager.getCurrentMediaId())
            var i = 0
            var offset = 0
            add@ while (i - offset <= 5) {
                val content = ((songCommentResponse.hotComments.getOrNull(i)
                        ?: songCommentResponse.comments.getOrNull(i - songCommentResponse.hotComments.size))
                        ?: break@add).content
                if (content.length > 30)
                    offset++
                else
                    viewBuilding.danmakuView.addBarrage(Barrage(content, false))
                i++
            }
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
                MediaManager.play()
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun setScreenBrightnessValue(brightnessValue: Float) {
        window.attributes = window.attributes.apply { screenBrightness = brightnessValue }
    }

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, TimeActivity::class.java))
    }
}