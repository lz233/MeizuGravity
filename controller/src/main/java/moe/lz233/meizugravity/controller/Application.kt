package moe.lz233.meizugravity.controller

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class Application : Application() {
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
    }
}