package moe.lz233.meizugravity.cloudmusic.logic.model.meta

import com.google.gson.annotations.SerializedName

data class Music(val id: Long,
                 @SerializedName("mv") val mvId: Long,
                 val name: String,
                 @SerializedName("ar") val artists: List<Artist>,
                 @SerializedName("al") val album: Album)

data class MusicUrl(val id: Long,
                    val url: String,
                    @SerializedName("br") val bitRate: Long,
                    val size: Long,
                    val type: String)