package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.MusicUrl

data class SongUrlResponse(val code: Int, val data: List<MusicUrl>)

data class SongLyricResponse(val code: Int,
                             @SerializedName("lrc") val lyric: LyricData,
                             @SerializedName("tlyric") val translatedLyric: LyricData) {
    data class LyricData(val version: Int, val lyric: String)
}