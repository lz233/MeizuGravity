package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object ServiceCreator {
    val BASE_HOST: String by lazy { URL(BaseDao.baseurl).host }

    private val baseOkHttpClient = OkHttpClient.Builder().build()

    private val dns = DnsOverHttps.Builder()
            .client(baseOkHttpClient)
            //.url(HttpUrl.get("https://doh.pub/dns-query"))
            .url(HttpUrl.get("https://dns.alidns.com/dns-query"))
            .build()

    val okHttpClient: OkHttpClient = baseOkHttpClient.newBuilder()
            .addInterceptor(RequestInterceptor())
            .dns(dns)
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BaseDao.baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}