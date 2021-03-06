package moe.lz233.meizugravity.cloudmusic.logic.model.meta

import com.google.gson.annotations.SerializedName

data class Music(val id: Long,
                 val name: String,
                 @SerializedName("ar") val artists: List<Artist>,
                 @SerializedName("al") val cover: MusicCover)

data class MusicCover(val id: Long, val name: String, val picUrl: String)