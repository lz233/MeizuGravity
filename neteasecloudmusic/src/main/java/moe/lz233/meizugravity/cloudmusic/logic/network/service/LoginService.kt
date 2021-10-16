package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.CheckQrResponse
import moe.lz233.meizugravity.cloudmusic.logic.model.response.KeyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("/eapi/login/qrcode/unikey")
    fun getKey(@Query("type") type: Int): Call<KeyResponse>

    @GET("/eapi/login/qrcode/client/login")
    fun checkQrStatus(@Query("key") key: String, @Query("type") type: Int): Call<CheckQrResponse>
}