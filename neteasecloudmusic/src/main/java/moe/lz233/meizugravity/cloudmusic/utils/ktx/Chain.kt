package moe.lz233.meizugravity.cloudmusic.utils.ktx

import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.utils.AESUtil
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.codec.digest.DigestUtils
import org.json.JSONObject

fun Interceptor.Chain.processEapi(): Response {
    val nobodyKnowThis = "36cd479b6b5"
    val url = this.request().url()
    val path = url.encodedPath().replace("eapi", "api")
    val json = JSONObject().apply {
        url.queryParameterNames().forEach {
            put(it, url.queryParameter(it))
        }
        put("header", JSONObject())
        put("e_r", false)
    }
    val nobodyUseMD5 = DigestUtils.md5Hex("nobody${path}use${json}md5forencrypt")
    val params = "$path-$nobodyKnowThis-$json-$nobodyKnowThis-$nobodyUseMD5"
    LogUtil.d(params)
    return this.proceed(Request.Builder()
            .addHeader("Cookie", "MUSIC_U=${UserDao.cookie}; appver=6.5.0; versioncode=164; buildver=${System.currentTimeMillis()}; os=android")
            .url(url)
            .post(FormBody.Builder()
                    .add("params", AESUtil.encrypt(params))
                    .build())
            .build())
}