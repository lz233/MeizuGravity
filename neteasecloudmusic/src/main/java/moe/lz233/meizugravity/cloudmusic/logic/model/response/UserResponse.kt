package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.PlayList
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.User

data class AccountInfoResponse(val code: Int, val profile: User)

data class UserPlaylistResponse(val code: Int, val version: String, val more: Boolean, @SerializedName("playlist") val playlists: List<PlayList>)