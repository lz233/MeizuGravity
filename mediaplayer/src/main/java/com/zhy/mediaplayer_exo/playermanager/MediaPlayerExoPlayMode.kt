package com.zhy.mediaplayer_exo.playermanager

/**
 * 媒体播放模式
 */
annotation class MediaPlayerExoPlayMode {
    companion object {
        //音频列表循环
        val MEDIA_LIST_LOOP = 1

        //音频单曲循环
        val MEDIA_ALONE_LOOP = 2

        //音频列表随机播放
        val MEDIA_LSIT_RANDOM = 3

        //音频列表顺序播放
        val MEDIA_LIST_ORDER_PLAY = 4
    }
}