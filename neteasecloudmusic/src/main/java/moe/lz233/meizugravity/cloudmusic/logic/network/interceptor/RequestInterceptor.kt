package moe.lz233.meizugravity.cloudmusic.logic.network.interceptor

import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.utils.ktx.processEapi
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.processEapi()
        response.headers("Set-Cookie").forEach {
            if (it.contains("MUSIC_U"))
                UserDao.cookie = it.substring(it.indexOf("MUSIC_U=") + 8, it.indexOf(';'))
        }
        return response
    }
}