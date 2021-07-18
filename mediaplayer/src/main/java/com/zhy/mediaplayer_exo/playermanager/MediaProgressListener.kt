package com.zhy.mediaplayer_exo.playermanager

/**
 * 音频进度监听
 */
interface MediaProgressListener {
    //当前媒体进度
    fun onProgressChange(position: Long, duration: Long)
}