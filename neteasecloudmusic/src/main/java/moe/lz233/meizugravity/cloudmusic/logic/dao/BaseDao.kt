package moe.lz233.meizugravity.cloudmusic.logic.dao

import moe.lz233.meizugravity.cloudmusic.App

object BaseDao {
    var baseUrl: String
        get() = App.sp.getString("baseUrl", "")!!
        set(value) = App.editor.putString("baseUrl", value).apply()

    var soundQuality: Long
        get() = App.sp.getLong("soundQuality", 999000)
        set(value) = App.editor.putLong("soundQuality", value).apply()
}