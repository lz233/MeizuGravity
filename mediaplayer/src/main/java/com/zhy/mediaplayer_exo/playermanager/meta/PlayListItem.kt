package com.zhy.mediaplayer_exo.playermanager.meta

import com.zhy.mediaplayer_exo.playermanager.PlaylistItem

class PlayListItem(override val id: Long,
                   override val mvId: Long,
                   override val name: String?,
                   override val artistId: List<Long>?,
                   override val artistName: String?,
                   override val albumId: Long?,
                   override val albumName: String?,
                   override val coverUrl: String?,
                   override val mediaUrl: String?) : PlaylistItem