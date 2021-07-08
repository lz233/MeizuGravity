package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.App
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private val BASE_URL = App.sp.getString("baseUrl", "https://netease-cloud-music-api-binaryify-wnhm9ka04-binaryify.vercel.app/")

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(RequestInterceptor()).build())
            .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}