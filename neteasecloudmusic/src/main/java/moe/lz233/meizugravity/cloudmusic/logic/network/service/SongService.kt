package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import moe.lz233.meizugravity.cloudmusic.logic.model.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {
    @GET("/eapi/song/enhance/player/url")
    fun getSongUrl(@Query("id") musicId: Long, @Query("br") bitRate: Int = BaseDao.soundQuality): Call<SongUrlResponse>

    @GET("/eapi/v1/mv/detail")
    fun getMvDetail(@Query("id") mvId: Long): Call<MvDetailResponse>

    @GET("/eapi/song/enhance/play/mv/url")
    fun getMvUrl(@Query("id") mvId: Long, @Query("r") resolution: Int): Call<MvUrlResponse>

    @GET("/eapi/song/lyric")
    fun getSongLyric(@Query("id") musicId: Long, @Query("lv") lv: Int = -1, @Query("kv") kv: Int = -1, @Query("tv") tv: Int = -1): Call<SongLyricResponse>

    @GET("/eapi/song/like")
    fun likeMusic(@Query("trackId") musicId: Long, @Query("like") operation: Boolean = true): Call<SongLikeResponse>
}