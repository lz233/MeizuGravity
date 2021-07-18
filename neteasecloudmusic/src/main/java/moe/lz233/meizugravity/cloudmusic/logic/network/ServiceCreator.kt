package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.App
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object ServiceCreator {
    private val BASE_URL = App.sp.getString("baseUrl", "https://netease-cloud-music-api-beta-five.vercel.app")
    val BASE_HOST by lazy { URL(BASE_URL).host }

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(RequestInterceptor()).build())
            .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}