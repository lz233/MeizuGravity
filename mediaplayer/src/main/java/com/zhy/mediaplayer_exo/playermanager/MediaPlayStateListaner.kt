package com.zhy.mediaplayer_exo.playermanager

/**
 * Created by zhy
 * Date 2021/2/25
 *
 * 音频状态监听
 *
 */
interface MediaPlayStateListaner {
    /**
     * state
     * exoplayer -> Player接口中
     */
    fun onMediaPlayState(state: Int)
}