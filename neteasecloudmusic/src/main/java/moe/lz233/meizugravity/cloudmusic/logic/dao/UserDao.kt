package moe.lz233.meizugravity.cloudmusic.logic.dao

import moe.lz233.meizugravity.design.App.Companion.editor
import moe.lz233.meizugravity.design.App.Companion.sp

object UserDao {
    var isLogin: Boolean
        get() = sp.getBoolean("userLogin", false)
        set(value) = editor.putBoolean("userLogin", value).apply()

    var id: Long
        get() = sp.getLong("userId", 0)
        set(value) = editor.putLong("userId", value).apply()

    var cookie: String
        get() = sp.getString("userCookie", "")!!
        set(value) = editor.putString("userCookie", value).apply()

    var name: String
        get() = sp.getString("userName", "")!!
        set(value) = editor.putString("userName", value).apply()

    var type: Int
        get() = sp.getInt("userType", 0)
        set(value) = editor.putInt("userType", value).apply()
}