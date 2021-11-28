package moe.lz233.meizugravity.cloudmusic.logic.network

import moe.lz233.meizugravity.cloudmusic.logic.network.service.*
import retrofit2.await

object CloudMusicNetwork {
    private val loginService = ServiceCreator.create(LoginService::class.java)
    private val userService = ServiceCreator.create(UserService::class.java)
    private val recommendService = ServiceCreator.create(RecommendService::class.java)
    private val playlistService = ServiceCreator.create(PlaylistService::class.java)
    private val songService = ServiceCreator.create(SongService::class.java)

    suspend fun getKey(type: Int) = loginService.getKey(type).await()
    suspend fun checkQrStatus(key: String, type: Int) = loginService.checkQrStatus(key, type).await()

    suspend fun getAccountInfo() = userService.getAccountInfo().await()
    suspend fun getUserPlaylist(userId: Long) = userService.getUserPlaylist(userId).await()
    suspend fun checkIn(platformCode: Int = 0) = userService.checkIn(platformCode).await()
    suspend fun musicianCheckIn() = userService.musicianCheckIn().await()
    suspend fun getMusicianTasks() = userService.getMusicianTasks().await()
    suspend fun obtainMusicianTask(userMissionId: Long, period: Int) = userService.obtainMusicianTask(userMissionId, period).await()

    suspend fun getDailyRecommendation() = recommendService.getDailyRecommendation().await()

    suspend fun getPlaylistDetail(playlistId: Long) = playlistService.getPlaylistDetail(playlistId).await()
    suspend fun Long.addMusicToPlaylist(playlistId: Long) = playlistService.modifyPlayListTracks("add", playlistId, "[$this]").await()
    suspend fun Long.removeMusicFromPlaylist(playlistId: Long) = playlistService.modifyPlayListTracks("del", playlistId, "[$this]").await()

    suspend fun getSongUrl(musicId: Long) = songService.getSongUrl(musicId).await()
    suspend fun getMvDetail(mvId: Long) = songService.getMvDetail(mvId).await()
    suspend fun getMvUrl(mvId: Long, resolution: Int = 720) = songService.getMvUrl(mvId, resolution).await()
    suspend fun getSongLyric(musicId: Long) = songService.getSongLyric(musicId).await()
    suspend fun getSongComment(musicId: Long) = songService.getSongComment(musicId).await()
    suspend fun Long.like() = songService.likeMusic(this).await()
}