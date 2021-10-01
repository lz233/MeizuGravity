package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.utils.AESUtil
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import okhttp3.*
import org.apache.commons.codec.digest.DigestUtils
import org.json.JSONObject

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val nobodyKnowThis = "36cd479b6b5"
        val url = chain.request().url()
        val path = url.encodedPath().replace("eapi", "api")
        val json = JSONObject().apply {
            url.queryParameterNames().forEach {
                put(it, url.queryParameter(it))
            }
            put("header", JSONObject())
            put("e_r", true)
        }
        val nobodyUseMD5 = DigestUtils.md5Hex("nobody${path}use${json}md5forencrypt")
        val params = "$path-$nobodyKnowThis-$json-$nobodyKnowThis-$nobodyUseMD5"
        LogUtil.d(params)
        val response = chain.proceed(Request.Builder()
                .addHeader("cookie", "MUSIC_U=${UserDao.cookie}")
                .url(url)
                .post(FormBody.Builder()
                        .add("params", AESUtil.encrypt(params))
                        .build())
                .build())
        val string = response.body()!!.string()
        LogUtil.d(string)
        LogUtil.d(AESUtil.decrypt(string))
        return response.newBuilder()
                .body(ResponseBody.create(null, JSONObject(AESUtil.decrypt(response.body()!!.string()))
                        .getJSONObject(path)
                        .toString()))
                .build()
    }
}