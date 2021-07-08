package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.CheckQrResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.KeyResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.QrResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.UserStatusResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("/login/qr/key")
    fun getKey(@Query("timestamp") timeStamp:Long):Call<KeyResponse>

    @GET("/login/qr/create")
    fun createQrCode(@Query("key") key:String,@Query("timestamp") timeStamp:Long):Call<QrResponse>

    @GET("/login/qr/check")
    fun checkQrStatus(@Query("key") key:String,@Query("timestamp") timeStamp:Long):Call<CheckQrResponse>

    @GET("/login/status")
    fun checkUserStatus(): Call<UserStatusResponse>
}