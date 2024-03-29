package moe.lz233.meizugravity.cloudmusic.utils.ktx

import com.zhy.mediaplayer_exo.playermanager.PlaylistItem
import com.zhy.mediaplayer_exo.playermanager.meta.PlayListItem
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Artist
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music

fun List<Music>.toPlayListItem() = mutableListOf<PlaylistItem>().apply {
    this@toPlayListItem.forEach {
        add(PlayListItem(it.id,
                it.mvId,
                it.name,
                it.artists.map { artist -> artist.id },
                it.artists.toArtistName(),
                it.album.id,
                it.album.name,
                it.album.picUrl,
                it.id.getSongUrl()))
    }
}

fun List<Artist>.toArtistName() = StringBuilder().apply {
    this@toArtistName.forEach {
        append(it.name)
        append('/')
    }
    deleteCharAt(lastIndex)
}.toString()

fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int) =
        if (toIndex > size)
            this.subList(fromIndex, size)
        else
            this.subList(fromIndex, toIndex)