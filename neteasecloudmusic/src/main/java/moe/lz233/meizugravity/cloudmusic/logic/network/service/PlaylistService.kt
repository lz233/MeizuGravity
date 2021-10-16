package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.ModifyPlayListTracksResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.response.PlaylistDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistService {
    @GET("/eapi/v6/playlist/detail")
    fun getPlaylistDetail(@Query("id") playlistId: Long, @Query("n") n: Int = 100000, @Query("s") s: Int = 0): Call<PlaylistDetailResponse>

    @GET("/eapi/playlist/manipulate/tracks")
    fun modifyPlayListTracks(@Query("op") operation: String, @Query("pid") playlistId: Long, @Query("trackIds") MusicIds: String, @Query("imme") imme: Boolean = true): Call<ModifyPlayListTracksResponse>
}