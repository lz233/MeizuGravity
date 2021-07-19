package moe.lz233.meizugravity.cloudmusic.utils.ktx

import com.zhy.mediaplayer_exo.playermanager.PlaylistItem
import com.zhy.mediaplayer_exo.playermanager.meta.PlayListItem
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music

fun List<Music>.toPlayListItem() = mutableListOf<PlaylistItem>().apply {
    this@toPlayListItem.forEach {
        add(PlayListItem(it.id, it.name, "", it.cover.picUrl, it.id.getSongUrl()))
    }
}