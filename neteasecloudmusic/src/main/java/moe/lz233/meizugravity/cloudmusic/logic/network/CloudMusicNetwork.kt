package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.network.service.LoginService
import retrofit2.await

object CloudMusicNetwork {
    private val loginService = ServiceCreator.create(LoginService::class.java)

    suspend fun getKey(timeStamp: Long) = loginService.getKey(timeStamp).await()
    suspend fun createQrCode(key: String, timeStamp: Long) = loginService.createQrCode(key, timeStamp).await()
    suspend fun checkQrStatus(key: String, timeStamp: Long) = loginService.checkQrStatus(key, timeStamp).await()
    suspend fun checkUserStatus(timeStamp: Long) = loginService.checkUserStatus(timeStamp).await()
}