package moe.lz233.meizugravity.cloudmusic.utils.ktx

import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import moe.lz233.meizugravity.cloudmusic.logic.network.ServiceCreator

fun Long.getSongUrl(bitRate: Long = BaseDao.soundQuality) = when (bitRate) {
    128000L -> "https://music.163.com/song/media/outer/url?id=$this.mp3"
    else -> "https://${ServiceCreator.BASE_HOST}/song/url?id=${this}&br=${bitRate}&redirect=true"
}