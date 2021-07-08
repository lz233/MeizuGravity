package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request().newBuilder()
                .header("cookie", "MUSIC_U=${UserDao.cookie}").build())
        return response
    }
}