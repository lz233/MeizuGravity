package moe.lz233.meizugravity.cloudmusic.logic.network.service

import moe.lz233.meizugravity.cloudmusic.logic.model.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/user/account")
    fun getAccountInfo(): Call<AccountInfoResponse>

    @GET("/user/playlist")
    fun getUserPlaylist(@Query("uid") userId: Long): Call<UserPlaylistResponse>

    @GET("/daily_signin")
    fun checkIn(@Query("type") platformCode: Int): Call<CheckInResponse>

    @GET("/yunbei/sign")
    fun yunbeiCheckIn(): Call<YunbeiCheckInResponse>

    @GET("/musician/sign")
    fun musicianCheckIn(): Call<MusicianCheckInResponse>

    @GET("/musician/tasks")
    fun getMusicianTasks(): Call<MusicianTasksResponse>

    @GET("/musician/cloudbean/obtain")
    fun obtainMusicianTask(@Query("id") userMissionId: Long, @Query("period") period: Int): Call<MusicianObtainTasksResponse>
}