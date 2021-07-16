package moe.lz233.meizugravity.cloudmusic.utils.ktx

import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao

fun String.adjustParam(width: String, height: String) = "$this?param=${width}y${height}"
fun String.isMyFavorite() = this == "${UserDao.name}喜欢的音乐"