package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName

data class KeyResponse(val code: Int, @SerializedName("unikey") val key: String)

data class CheckQrResponse(val code: Int, val message: String, val cookie: String)