package moe.lz233.meizugravity.cloudmusic.utils.ktx

import moe.lz233.meizugravity.cloudmusic.logic.network.ServiceCreator

fun Long.getSongUrl(bitRate: Long = 999000) = "https://${ServiceCreator.BASE_HOST}/song/url?id=${this}&br=${bitRate}&redirect=true"