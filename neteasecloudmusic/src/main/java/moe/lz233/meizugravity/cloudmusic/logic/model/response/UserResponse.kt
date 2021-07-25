package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.PlayList
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.User

data class AccountInfoResponse(val code: Int, val profile: User)

data class UserPlaylistResponse(val code: Int, val version: String, val more: Boolean, @SerializedName("playlist") val playlists: List<PlayList>)

data class CheckInResponse(val code: Int, val point: Int?, @SerializedName("msg") val message: String?)

data class YunbeiCheckInResponse(val code: Int, val point: Int?, @SerializedName("msg") val message: String?)

data class MusicianCheckInResponse(val code: Int)

data class MusicianTasksResponse(val code: Int, val data: MusicianTasksList) {
    data class MusicianTasksList(@SerializedName("list") val tasks: List<MusicianTask>) {
        data class MusicianTask(@SerializedName("description") val name: String,
                                val userMissionId: Long?,
                                val status: Int?,
                                val period: Int,
                                val rewardWorth: String)
    }
}

data class MusicianObtainTasksResponse(val code: Int, val message: String)