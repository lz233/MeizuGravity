package moe.lz233.meizugravity.cloudmusic.utils.ktx

import moe.lz233.meizugravity.cloudmusic.logic.dao.BaseDao
import moe.lz233.meizugravity.cloudmusic.logic.network.ServiceCreator

fun Long.getSongUrl(bitRate: Int = BaseDao.soundQuality) = "${ServiceCreator.BASE_URL}/eapi/song/enhance/player/url?ids=[$this]&br=$bitRate"