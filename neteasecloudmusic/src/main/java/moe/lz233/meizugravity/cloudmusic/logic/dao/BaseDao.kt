package moe.lz233.meizugravity.cloudmusic.logic.dao

import moe.lz233.meizugravity.design.App.Companion.editor
import moe.lz233.meizugravity.design.App.Companion.sp

object BaseDao {
    var soundQuality: Int
        get() = sp.getInt("soundQuality", 999000)
        set(value) = editor.putInt("soundQuality", value).apply()

    var screenBrightness: Float
        get() = sp.getFloat("screenBrightness", 0.1f)
        set(value) = editor.putFloat("screenBrightness", value).apply()
}