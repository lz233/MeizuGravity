package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/eapi/nuser/account/get")
    fun getAccountInfo(): Call<AccountInfoResponse>

    @GET("/eapi/user/playlist")
    fun getUserPlaylist(@Query("uid") userId: Long, @Query("limit") limit: Int = 30, @Query("offset") offset: Int = 0): Call<UserPlaylistResponse>

    @GET("/eapi/point/dailyTask")
    fun checkIn(@Query("type") platformCode: Int): Call<CheckInResponse>

    @GET("/eapi/creator/user/access")
    fun musicianCheckIn(): Call<MusicianCheckInResponse>

    @GET("/eapi/nmusician/workbench/mission/cycle/list")
    fun getMusicianTasks(): Call<MusicianTasksResponse>

    @GET("/eapi/nmusician/workbench/mission/reward/obtain/new")
    fun obtainMusicianTask(@Query("userMissionId") userMissionId: Long, @Query("period") period: Int): Call<MusicianObtainTasksResponse>
}