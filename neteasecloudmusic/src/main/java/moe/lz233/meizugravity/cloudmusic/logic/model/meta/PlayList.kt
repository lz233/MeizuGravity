package moe.lz233.meizugravity.cloudmusic.logic.model.meta

import com.google.gson.annotations.SerializedName

data class PlayList(val id: Long, val name: String, @SerializedName("trackCount") val musicCount: Int, val coverImgUrl: String, val creator: User)
