package moe.lz233.meizugravity.cloudmusic.logic.model

import com.google.gson.annotations.SerializedName

data class KeyResponse(val code: Int, val data: KeyData) {
    data class KeyData(val code: Int, @SerializedName("unikey") val key: String)
}

data class QrResponse(val code: Int, val data: QrData) {
    data class QrData(@SerializedName("qrurl") val qrUrl: String, @SerializedName("qrimg") val qrImg: String)
}

data class CheckQrResponse(val code: Int, val message: String, val cookie: String)

data class UserStatusResponse(val data: UserStatusData) {
    data class UserStatusData(val code: Int, val profile: ProfileData)
    data class ProfileData(val userID: Long,
                           @SerializedName("nickname") val nickName: String,
                           val avatarUrl: String,
                           @SerializedName("signature") val bio: String)
}