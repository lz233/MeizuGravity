package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.SongLyricResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.response.SongUrlResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {
    @GET("/song/url")
    fun getSongUrl(@Query("id") musicId: Long): Call<SongUrlResponse>

    @GET("/song/url")
    fun getSongUrlDirectly(@Query("id") musicId: Long, @Query("direct") direct: Boolean = true): Call<*>

    @GET("/lyric")
    fun getSongLyric(@Query("id") musicId: Long): Call<SongLyricResponse>
}