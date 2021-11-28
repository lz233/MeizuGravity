package com.zhy.mediaplayer_exo.playermanager.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.zhy.mediaplayer_exo.playermanager.*
import com.zhy.mediaplayer_exo.playermanager.musicbroadcast.MusicBroadcast
import com.zhy.mediaplayer_exo.playermanager.service.MediaForegroundService

object MediaManager : Player.EventListener {
    private lateinit var mContext: Context
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    //音频播放列表
    var playlistItemList = mutableListOf<PlaylistItem>()

    //音频进度列表
    private val infProgressList = mutableListOf<MediaProgressListener>()

    //音频错误回调列表
    private val infErrorListenerList = mutableListOf<MediaErrorListener>()

    //音频改变接口列表
    private val infMediaSwitchTrackChangeListenerList = mutableListOf<MediaSwitchTrackChange>()

    //音频状态接口列表
    private val infMediaPlayerStateListenerList = mutableListOf<MediaPlayStateListaner>()

    //是否初始化
    var isInit = false

    //当前播放模式
    private var currentPlayMode = MediaPlayerExoPlayMode.MEDIA_LIST_LOOP

    /**
     * media管理者初始化
     *
     * @param context 环境上下文
     */
    fun init(context: Context, exoPlayer: SimpleExoPlayer) {
        this.mContext = context
        simpleExoPlayer = exoPlayer
        simpleExoPlayer.addListener(this)
        isInit = true
    }

    /**
     * 播放列表（按顺序播放）
     *
     * @param mutableList 播放列表
     */
    @JvmOverloads
    fun playlist(mutableList: MutableList<PlaylistItem>, playIndex: Int = 0) {
        checkRestartService()
        if (playIndex >= mutableList.size || playIndex < 0) throw IllegalArgumentException("播放索引不正确，找不到任何音频项目")
        playlistItemList = mutableList
        val mediaItemList = mutableListOf<MediaItem>()
        mutableList.map {
            mediaItemList.add(
                    MediaItem.Builder()
                            .setUri(Uri.parse(it.mediaUrl))
                            .setMediaId(it.id.toString())
                            .build()
            )
        }
        simpleExoPlayer.setMediaItems(mediaItemList)
        infMediaSwitchTrackChangeListenerList.map {
            it.onTracksChange(playlistItemList[0])
        }
        switchPlayMode(currentPlayMode)
        simpleExoPlayer.prepare()
        simpleExoPlayer.seekToDefaultPosition(playIndex)
        simpleExoPlayer.play()
    }

    /**
     * {@see #MediaPlayerExoPlayMode}
     *
     * 切换音频播放模式
     */
    fun switchPlayMode(@MediaPlayerExoPlayMode mode: Int) {
        currentPlayMode = mode
        when (mode) {
            MediaPlayerExoPlayMode.MEDIA_ALONE_LOOP -> {
                simpleExoPlayer.shuffleModeEnabled = false
                simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            }
            MediaPlayerExoPlayMode.MEDIA_LIST_LOOP -> {
                simpleExoPlayer.shuffleModeEnabled = false
                simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ALL
            }
            MediaPlayerExoPlayMode.MEDIA_LIST_ORDER_PLAY -> {
                simpleExoPlayer.shuffleModeEnabled = false
                simpleExoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            }
            MediaPlayerExoPlayMode.MEDIA_LSIT_RANDOM -> {
                simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                simpleExoPlayer.shuffleModeEnabled = true
            }
        }
    }

    @JvmOverloads
    fun seekToPositionIndex(index: Int, position: Long) {
        simpleExoPlayer.seekTo(index, position)
    }


    /**
     * 音频进度监听
     *
     * @param current 当前进度
     */
    fun invokeProgressListenerList(current: Long) {
        infProgressList.map {
            it.onProgressChange(current, simpleExoPlayer.duration)
        }
    }

    /**
     * 添加进度监听
     */
    fun addProgressListener(mediaProgressListener: MediaProgressListener) {
        infProgressList.add(mediaProgressListener)
    }

    /**
     * 添加音频播放状态监听
     */
    fun addMediaPlayerStateListener(mediaPlayStateListaner: MediaPlayStateListaner) {
        infMediaPlayerStateListenerList.add(mediaPlayStateListaner)
    }

    /**
     * 添加音频错误监听
     */
    fun addMediaErrorListener(mediaErrorListener: MediaErrorListener) {
        infErrorListenerList.add(mediaErrorListener)
    }

    /**
     * 移除音频进度监听
     */
    fun removeProgressListener(mediaProgressListener: MediaProgressListener) {
        infProgressList.remove(mediaProgressListener)
    }

    /**
     * 移除音频错误监听
     */
    fun removeErrorListener(mediaErrorListener: MediaErrorListener) {
        infErrorListenerList.remove(mediaErrorListener)
    }

    /**
     * 移除音频改变监听
     */
    fun removeMediaSwitchChange(mediaSwitchTrackChange: MediaSwitchTrackChange) {
        infMediaSwitchTrackChangeListenerList.remove(mediaSwitchTrackChange)
    }

    /**
     * 添加音频改变监听
     */
    fun addMediaSwitchChange(mediaSwitchTrackChange: MediaSwitchTrackChange) {
        infMediaSwitchTrackChangeListenerList.add(mediaSwitchTrackChange)
    }

    /**
     * 播放下一首
     */
    fun playNext(): Boolean {
        checkRestartService()
        val f = simpleExoPlayer.hasNext()
        if (f) {
            simpleExoPlayer.next()
        }
        return f
    }

    /**
     * 播放上一首
     */
    fun playLast(): Boolean {
        checkRestartService()
        val f = simpleExoPlayer.hasPrevious()
        if (simpleExoPlayer.hasPrevious()) {
            simpleExoPlayer.previous()
        }
        return f
    }

    /**
     * 播放
     */
    fun play() {
        checkRestartService()
        simpleExoPlayer.play()
    }

    /**
     * 暂停
     */
    fun pause() {
        checkRestartService()
        simpleExoPlayer.pause()
    }

    /**
     * 播放切换
     */
    fun playOrPause() {
        checkRestartService()
        if (simpleExoPlayer.isPlaying) {
            simpleExoPlayer.pause()
        } else {
            simpleExoPlayer.play()
        }
    }

    private fun checkRestartService() {
        val className = MediaForegroundService.javaClass.name
        if (!MediaForegroundService.isServiceWork(
                        mContext,
                        className.substring(0, className.lastIndexOf("$"))
                )
        ) {
            val serviceIntent = Intent(mContext, MediaForegroundService::class.java).apply {
                action = "${mContext.packageName}.mediaplayer.service.action"
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(serviceIntent)
            } else {
                mContext.startService(serviceIntent)
            }
        }
    }

    /**
     * 是否正在播放
     */
    fun isPlaying() = simpleExoPlayer.isPlaying

    /**
     * 获取当前播放id
     */
    fun currentId() = simpleExoPlayer.currentMediaItem?.mediaId

    /**
     * 获取播放实例
     */
    fun getSimpleExoPlayer(): SimpleExoPlayer = simpleExoPlayer

    /**
     * 获取播放总进度
     */
    fun getDuration() = simpleExoPlayer.duration

    /**
     * 获取当前播放进度
     */
    fun getCurrentPosition() = simpleExoPlayer.currentPosition

    /**
     * 获取当前音频 MV ID
     */
    fun getCurrentMediaMvId(): Long {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].mvId
    }

    /**
     * 获取当前音频名称
     */
    fun getCurrentMediaName(): String {
        if (playlistItemList.isNotEmpty())
            return playlistItemList[simpleExoPlayer.currentWindowIndex].name ?: ""
        else
            return "未知音频"
    }

    /**
     * 获取当前音频封面地址
     */
    fun getCurrentMediaCover(): String {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].coverUrl ?: ""
    }

    /**
     * 获取当前音频艺术家ID
     */
    fun getCurrentMediaArtistId(): List<Long> {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].artistId ?: listOf()
    }

    /**
     * 获取当前音频艺术家名称
     */
    fun getCurrentMediaArtistName(): String {
        if (playlistItemList.isNotEmpty())
            return playlistItemList[simpleExoPlayer.currentWindowIndex].artistName ?: ""
        else
            return "未知"
    }

    /**
     * 获取当前音频ID
     */
    fun getCurrentMediaId() = playlistItemList[simpleExoPlayer.currentWindowIndex].id

    /**
     * 获取当前音频专辑ID
     */
    fun getCurrentMediaAlbumId(): Long {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].albumId ?: 0
    }

    /**
     * 获取当前音频专辑名称
     */
    fun getCurrentMediaAlbumName(): String {
        if (playlistItemList.isNotEmpty())
            return playlistItemList[simpleExoPlayer.currentWindowIndex].albumName ?: ""
        else
            return "未知"
    }

    /**
     * 获取当前音频
     */
    fun getCurrentMedia() = playlistItemList[simpleExoPlayer.currentWindowIndex]

    /**
     * 获取当前播放模式
     *
     * @link {#MediaPlayerExoPlayMode}
     */
    fun getCurrentPlayMode(): Int {
        return currentPlayMode
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK || reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
            //已自动切换到下一个
            val list = playlistItemList.filter {
                it.id.toString() == mediaItem?.mediaId
            }
            if (!list.isNullOrEmpty()) {
                infMediaSwitchTrackChangeListenerList.map {
                    it.onTracksChange(list[0])
                }
            }
            mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                        MusicBroadcast.EXTRA_ACTION,
                        MusicBroadcast.PENDINGINTENT_READY_PLAY_CLICK
                )
            })

        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        super.onTimelineChanged(timeline, reason)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        infMediaPlayerStateListenerList.map {
            if (isPlaying) {
                it.onMediaPlayState(Player.STATE_READY)
            } else {
                it.onMediaPlayState(Player.STATE_ENDED)
            }
        }
        if (isPlaying) {
            mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                        MusicBroadcast.EXTRA_ACTION,
                        MusicBroadcast.PENDINGINTENT_READY_PLAY_CLICK
                )
            })
        } else {
            mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                        MusicBroadcast.EXTRA_ACTION,
                        MusicBroadcast.PENDINGINTENT_NO_READY_PLAY_CLICK
                )
            })
        }
    }


    override fun onPlaybackStateChanged(state: Int) {
        when (state) {
            Player.STATE_READY -> {
                mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                    putExtra(
                            MusicBroadcast.EXTRA_ACTION,
                            MusicBroadcast.PENDINGINTENT_READY_PLAY_CLICK
                    )
                })
            }

            else -> mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                        MusicBroadcast.EXTRA_ACTION,
                        MusicBroadcast.PENDINGINTENT_NO_READY_PLAY_CLICK
                )
            })
        }
    }

    override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
    ) {
        super.onTracksChanged(trackGroups, trackSelections)
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        error.printStackTrace()
        if (playNext()) {
            simpleExoPlayer.prepare()
            simpleExoPlayer.play()
        }
        infErrorListenerList.map {
            it.onMediaError()
        }
    }

}