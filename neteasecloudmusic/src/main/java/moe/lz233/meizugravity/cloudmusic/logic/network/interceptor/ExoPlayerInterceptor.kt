package moe.lz233.meizugravity.cloudmusic.logic.network.interceptor

import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ktx.processEapi
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

class ExoPlayerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (chain.request().url().encodedPath().contains("eapi")) {
            val response = chain.processEapi()
            val body = response.body()!!.string()
            val url = JSONObject(body)
                    .getJSONArray("data")
                    .getJSONObject(0)
                    .getString("url")
            LogUtil.d("music url: $url")
            chain.proceed(chain.request().newBuilder()
                    .url(url)
                    .get()
                    .build())
        } else {
            chain.proceed(chain.request())
        }
    }
}