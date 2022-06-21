package moe.lz233.meizugravity.cloudmusic.logic.network.interceptor

import moe.lz233.meizugravity.design.utils.LogUtil
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        LogUtil.d(message)
    }
}