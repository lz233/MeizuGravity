package moe.lz233.meizugravity.cloudmusic.ui.daily

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music

class DailyAdapter(private val activity: Activity, songList: List<Music>) : ArrayAdapter<Music>(activity, R.layout.item_music, songList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
                ?: LayoutInflater.from(activity).inflate(R.layout.item_music, parent, false)
        val music = getItem(position)!!
        view.findViewById<TextView>(R.id.musicTextView).text = music.name
        view.findViewById<TextView>(R.id.artistTextView).text = StringBuilder().apply {
            for (artist in music.artists) {
                append(artist.name)
                append('/')
            }
            deleteCharAt(lastIndex)
        }
        return view
    }
}