package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.AccountInfoResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("/user/account")
    fun getAccountInfo(): Call<AccountInfoResponse>
}