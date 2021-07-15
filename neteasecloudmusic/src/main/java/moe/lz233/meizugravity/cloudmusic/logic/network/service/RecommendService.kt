package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.DailyRecommendationResponse
import retrofit2.Call
import retrofit2.http.GET

interface RecommendService {
    @GET("/recommend/songs")
    fun getDailyRecommendation(): Call<DailyRecommendationResponse>
}