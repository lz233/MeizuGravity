package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.PlaylistDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistService {
    @GET("/playlist/detail")
    fun getPlaylistDetail(@Query("id") playlistId: Long): Call<PlaylistDetailResponse>
}