package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object ServiceCreator {
    val BASE_HOST by lazy { URL(BaseDao.baseurl).host }

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .build()

    private val dns = DnsOverHttps.Builder()
            .client(okHttpClient)
            .url(HttpUrl.get("https://doh.pub/dns-query"))
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BaseDao.baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.newBuilder()
                    .dns(dns)
                    .build())
            .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}