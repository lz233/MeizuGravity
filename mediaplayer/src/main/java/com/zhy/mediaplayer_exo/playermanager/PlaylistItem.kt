package com.zhy.mediaplayer_exo.playermanager

/**
 * 播放item接口
 * 任何音频数据结构需要实现此接口
 */
interface PlaylistItem {
    val id: Long
    val mvId: Long
    val name: String?
    val artistId: List<Long>?
    val artistName: String?
    val albumId: Long?
    val albumName: String?
    val coverUrl: String?
    val mediaUrl: String?
}