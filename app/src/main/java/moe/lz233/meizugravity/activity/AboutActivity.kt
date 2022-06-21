package moe.lz233.meizugravity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import moe.lz233.meizugravity.BuildConfig
import moe.lz233.meizugravity.databinding.ActivityAboutBinding
import moe.lz233.meizugravity.design.activity.BaseActivity
import moe.lz233.meizugravity.design.utils.QRCodeUtil

class AboutActivity : BaseActivity() {

    private val viewBuilding by lazy { ActivityAboutBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        viewBuilding.aboutImageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap("https://github.com/lz233/MeizuGravity", 100, 100))
        viewBuilding.versionTextView.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, AboutActivity::class.java))
    }
}