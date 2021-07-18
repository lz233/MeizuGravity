package com.zhy.mediaplayer_exo.playermanager

/**
 * 音频声音改变
 */
interface MediaSwitchTrackChange {
    fun onTracksChange(playlistItem: PlaylistItem)
}