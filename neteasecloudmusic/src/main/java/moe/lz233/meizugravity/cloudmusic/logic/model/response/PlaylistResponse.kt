package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.PlayList

data class PlaylistDetailResponse(val code: Int, val playlist: PlayList)

data class ModifyPlayListTracksResponse(val code: Int, @SerializedName("body") val data: ModifyPlayListTracksData) {
    data class ModifyPlayListTracksData(val code: Int, val message: String)
}