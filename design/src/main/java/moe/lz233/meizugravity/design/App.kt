package moe.lz233.meizugravity.design

import android.content.Context
import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication

open class App(private val spName: String) : MultiDexApplication() {
    companion object {
        lateinit var context: Context
        lateinit var sp: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        sp = context.getSharedPreferences(spName, MODE_PRIVATE)
        editor = sp.edit()
    }
}