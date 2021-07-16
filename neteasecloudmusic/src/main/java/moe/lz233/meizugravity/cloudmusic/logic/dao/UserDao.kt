package moe.lz233.meizugravity.cloudmusic.logic.dao

import moe.lz233.meizugravity.cloudmusic.App

object UserDao {
    var isLogin: Boolean
        get() = App.sp.getBoolean("userLogin", false)
        set(value) = App.editor.putBoolean("userLogin", value).apply()

    var id: Long
        get() = App.sp.getLong("userId", 0)
        set(value) = App.editor.putLong("userId", value).apply()

    var cookie: String
        get() = App.sp.getString("userCookie", "")!!
        set(value) = App.editor.putString("userCookie", value).apply()

    var name: String
        get() = App.sp.getString("userName", "")!!
        set(value) = App.editor.putString("userName", value).apply()
}