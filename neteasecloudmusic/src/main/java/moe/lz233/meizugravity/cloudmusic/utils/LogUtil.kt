package moe.lz233.meizugravity.cloudmusic.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import moe.lz233.meizugravity.cloudmusic.App
import android.util.Log as ALog

object LogUtil {

    private const val maxLength = 4000
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    @JvmOverloads
    fun toast(msg: String, duration: Int = Toast.LENGTH_SHORT, force: Boolean = true) {
        if (!force) return
        handler.post { ToastUtil.show(App.context, msg, duration) }
    }

    @JvmStatic
    private fun doLog(f: (String, String) -> Int, obj: Any?) {
        val str = if (obj is Throwable) ALog.getStackTraceString(obj) else obj.toString()
        if (str.length > maxLength) {
            val chunkCount: Int = str.length / maxLength
            for (i in 0..chunkCount) {
                val max: Int = 4000 * (i + 1)
                if (max >= str.length) {
                    doLog(f, str.substring(maxLength * i))
                } else {
                    doLog(f, str.substring(maxLength * i, max))
                }
            }
        } else {
            f("CLOUDMUSIC", str)
            toast(str, force = false)
        }
    }

    @JvmStatic
    fun d(obj: Any?) {
        doLog(ALog::d, obj)
    }

    @JvmStatic
    fun i(obj: Any?) {
        doLog(ALog::i, obj)
    }

    @JvmStatic
    fun e(obj: Any?) {
        doLog(ALog::e, obj)
    }

    @JvmStatic
    fun v(obj: Any?) {
        doLog(ALog::v, obj)
    }

    @JvmStatic
    fun w(obj: Any?) {
        doLog(ALog::w, obj)
    }
}