package moe.lz233.meizugravity.cloudmusic.logic.model.meta

data class PlayList(val id: Long, val name: String, val trackCount: Int, val coverImgUrl: String, val creator: User, val tracks: List<Music>)
