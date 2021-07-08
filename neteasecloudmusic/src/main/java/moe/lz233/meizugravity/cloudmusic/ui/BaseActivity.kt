package moe.lz233.meizugravity.cloudmusic.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import moe.lz233.meizugravity.cloudmusic.BuildConfig
import moe.lz233.meizugravity.cloudmusic.utils.SystemPropertyUtil

abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = findViewById<View>(android.R.id.content)
        //横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //检测设备
        if (!((SystemPropertyUtil.getSystemProperty("ro.udisk.lable") == "A8") or BuildConfig.DEBUG)) {
            AlertDialog.Builder(this).apply {
                setMessage("?!")
            }.create().apply {
                setOnDismissListener { finish() }
                show()
            }
        }
        //全局自定义字体
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder()
                        .setDefaultFontPath("font.otf")
                        .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                        .build()))
                .build())
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}