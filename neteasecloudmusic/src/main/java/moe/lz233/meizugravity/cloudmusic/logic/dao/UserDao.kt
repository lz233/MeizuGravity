package moe.lz233.meizugravity.cloudmusic.logic.dao

import moe.lz233.meizugravity.cloudmusic.App

object UserDao {
    var isLogin: Boolean
        get() = App.sp.getBoolean("userLogin", false)
        set(value) = App.editor.putBoolean("userLogin", value).apply()

    var cookie: String
        get() = App.sp.getString("userCookie", "")!!
        set(value) = App.editor.putString("userCookie", value).apply()
}