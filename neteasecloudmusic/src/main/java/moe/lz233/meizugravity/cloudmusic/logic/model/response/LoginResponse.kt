package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.User

data class KeyResponse(val code: Int, @SerializedName("unikey") val key: String)

data class CheckQrResponse(val code: Int, val message: String, val cookie: String)

data class UserStatusResponse(val data: UserStatusData) {
    data class UserStatusData(val code: Int, val profile: User)
}