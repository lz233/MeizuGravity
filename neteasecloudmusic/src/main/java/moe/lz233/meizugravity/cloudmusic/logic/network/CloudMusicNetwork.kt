package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.network.service.LoginService
import moe.lz233.meizugravity.cloudmusic.logic.network.service.PlaylistService
import moe.lz233.meizugravity.cloudmusic.logic.network.service.RecommendService
import moe.lz233.meizugravity.cloudmusic.logic.network.service.UserService
import retrofit2.await

object CloudMusicNetwork {
    private val loginService = ServiceCreator.create(LoginService::class.java)
    private val userService = ServiceCreator.create(UserService::class.java)
    private val recommendService = ServiceCreator.create(RecommendService::class.java)
    private val playlistService = ServiceCreator.create(PlaylistService::class.java)

    suspend fun getKey(timeStamp: Long) = loginService.getKey(timeStamp).await()
    suspend fun createQrCode(key: String, timeStamp: Long) = loginService.createQrCode(key, timeStamp).await()
    suspend fun checkQrStatus(key: String, timeStamp: Long) = loginService.checkQrStatus(key, timeStamp).await()
    suspend fun checkUserStatus() = loginService.checkUserStatus().await()

    suspend fun getAccountInfo() = userService.getAccountInfo().await()
    suspend fun getUserPlaylist(userId: Long) = userService.getUserPlaylist(userId).await()

    suspend fun getDailyRecommendation() = recommendService.getDailyRecommendation().await()

    suspend fun getPlaylistDetail(playlistId: Long) = playlistService.getPlaylistDetail(playlistId).await()
}