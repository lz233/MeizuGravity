package moe.lz233.meizugravity.cloudmusic.utils.ktx

import com.zhy.mediaplayer_exo.playermanager.PlaylistItem
import com.zhy.mediaplayer_exo.playermanager.meta.PlayListItem
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music

fun List<Music>.toPlayListItem() = mutableListOf<PlaylistItem>().apply {
    this@toPlayListItem.forEach {
        add(PlayListItem(it.id, it.name, it.artists.map {
            it.id
        }, StringBuilder().apply {
            it.artists.forEach {
                append(it.name)
                append('/')
            }
            deleteCharAt(lastIndex)
        }.toString(), it.album.id, it.album.name, it.album.picUrl, it.id.getSongUrl()))
    }
}