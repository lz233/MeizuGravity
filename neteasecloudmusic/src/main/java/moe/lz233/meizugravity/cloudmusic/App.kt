package moe.lz233.meizugravity.cloudmusic

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import org.conscrypt.Conscrypt
import java.security.Security
import javax.net.ssl.SSLContext

class App : MultiDexApplication() {
    companion object {
        lateinit var context: Context
        lateinit var sp: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        sp = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        editor = sp.edit()
        Security.insertProviderAt(Conscrypt.newProvider(),1)
    }
}