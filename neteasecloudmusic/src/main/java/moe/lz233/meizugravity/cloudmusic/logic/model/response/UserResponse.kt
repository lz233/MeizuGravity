package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName

data class AccountInfoResponse(val code: Int, val profile: ProfileData) {
    data class ProfileData(val userId: Long,
                           @SerializedName("nickname") val nickName: String,
                           val avatarUrl: String,
                           @SerializedName("signature") val bio: String)
}