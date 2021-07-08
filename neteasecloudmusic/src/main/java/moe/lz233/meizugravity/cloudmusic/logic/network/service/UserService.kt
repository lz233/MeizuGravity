package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.KeyResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.QrResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/login/qr/key")
    fun getKey(@Query("timestamp") timeStamp:Long):Call<KeyResponse>
}