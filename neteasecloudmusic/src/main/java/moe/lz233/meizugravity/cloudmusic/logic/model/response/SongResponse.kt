package moe.lz233.meizugravity.cloudmusic.logic.model.response

import moe.lz233.meizugravity.cloudmusic.logic.model.meta.MusicUrl

data class SongUrlResponse(val code: Int, val data: List<MusicUrl>)