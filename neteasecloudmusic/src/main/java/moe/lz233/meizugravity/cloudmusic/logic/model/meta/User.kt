package moe.lz233.meizugravity.cloudmusic.logic.model.meta

import com.google.gson.annotations.SerializedName

data class User(val userId: Long,
                @SerializedName("nickname") val nickName: String,
                val avatarUrl: String,
                @SerializedName("signature") val bio: String)
