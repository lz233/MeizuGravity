package moe.lz233.meizugravity.cloudmusic.logic.dao

import moe.lz233.meizugravity.cloudmusic.App

object BaseDao {
    var soundQuality: Int
        get() = App.sp.getInt("soundQuality", 999000)
        set(value) = App.editor.putInt("soundQuality", value).apply()
}