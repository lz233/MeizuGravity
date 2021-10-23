package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import moe.lz233.meizugravity.cloudmusic.logic.model.response.SongLikeResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.response.SongLyricResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.response.SongUrlResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {
    @GET("/eapi/song/enhance/player/url")
    fun getSongUrl(@Query("id") musicId: Long, @Query("br") bitRate: Int = BaseDao.soundQuality): Call<SongUrlResponse>

    @GET("/eapi/song/lyric")
    fun getSongLyric(@Query("id") musicId: Long, @Query("lv") lv: Int = -1, @Query("kv") kv: Int = -1, @Query("tv") tv: Int = -1): Call<SongLyricResponse>

    @GET("/eapi/song/like")
    fun likeMusic(@Query("trackId") musicId: Long, @Query("like") operation: Boolean = true): Call<SongLikeResponse>
}