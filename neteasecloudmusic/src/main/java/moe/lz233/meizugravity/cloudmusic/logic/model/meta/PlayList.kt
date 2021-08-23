package moe.lz233.meizugravity.cloudmusic.logic.model.meta

data class PlayList(val id: Long,
                    val name: String,
                    val trackCount: Int,
                    val specialType: Int, //官方动态歌单是 100 来着
                    val coverImgUrl: String,
                    val creator: User,
                    val tracks: List<Music>)
