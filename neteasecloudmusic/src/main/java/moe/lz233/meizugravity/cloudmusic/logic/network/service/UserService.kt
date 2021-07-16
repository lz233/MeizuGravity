package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.AccountInfoResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.response.UserPlaylistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/user/account")
    fun getAccountInfo(): Call<AccountInfoResponse>

    @GET("/user/playlist")
    fun getUserPlaylist(@Query("uid") userId: Long): Call<UserPlaylistResponse>
}