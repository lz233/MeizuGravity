package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music

data class DailyRecommendationResponse(val code: Int, val data: DailyRecommendationData) {
    data class DailyRecommendationData(@SerializedName("dailySongs") val songs: List<Music>)
}
