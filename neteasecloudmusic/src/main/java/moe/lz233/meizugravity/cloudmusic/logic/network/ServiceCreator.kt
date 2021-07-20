package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object ServiceCreator {
    val BASE_HOST by lazy { URL(BaseDao.baseurl).host }

    private val retrofit = Retrofit.Builder()
            .baseUrl(BaseDao.baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(RequestInterceptor()).build())
            .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}