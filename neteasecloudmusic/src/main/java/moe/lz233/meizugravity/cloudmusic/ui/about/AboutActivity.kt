package moe.lz233.meizugravity.cloudmusic.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import moe.lz233.meizugravity.cloudmusic.App
import moe.lz233.meizugravity.cloudmusic.BuildConfig
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityAboutBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.QRCodeUtil
import kotlin.system.exitProcess

class AboutActivity : BaseActivity() {
    private val viewBuilding by lazy { ActivityAboutBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        viewBuilding.aboutImageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap("https://github.com/lz233/MeizuGravity/blob/master/NETEASECLOUDMUSIC.md", 100, 100))
        viewBuilding.versionTextView.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        viewBuilding.soundQualityButton.setOnClickListener {
            when (BaseDao.soundQuality) {
                999000L -> {
                    BaseDao.soundQuality = 128000
                    LogUtil.toast("当前音质：低音质")
                }
                320000L -> {
                    BaseDao.soundQuality = 999000
                    LogUtil.toast("当前音质：无损")
                }
                128000L -> {
                    BaseDao.soundQuality = 320000
                    LogUtil.toast("当前音质：高音质")
                }
            }
        }
        viewBuilding.logoutButton.setOnClickListener {
            App.editor.remove("userLogin").commit()
            exitProcess(0)
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

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, AboutActivity::class.java))
    }
}