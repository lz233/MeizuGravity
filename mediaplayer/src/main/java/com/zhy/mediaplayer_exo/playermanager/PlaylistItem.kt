package com.zhy.mediaplayer_exo.playermanager

/**
 * 播放item接口
 * 任何音频数据结构需要实现此接口
 */
interface PlaylistItem {
    val id: Long
    val title: String?
    val intro: String?
    val coverUrl: String?
    val mediaUrl: String?
}