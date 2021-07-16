package moe.lz233.meizugravity.cloudmusic.ui.playlist

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.PlayList

class PlayListAdapter(private val activity: Activity, playLists: List<PlayList>) : ArrayAdapter<PlayList>(activity, R.layout.item_playlist, playLists) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
                ?: LayoutInflater.from(activity).inflate(R.layout.item_playlist, parent, false)
        val playList = getItem(position)!!
        view.findViewById<TextView>(R.id.playlistTitleTextView).text = playList.name
        view.findViewById<TextView>(R.id.playlistSummaryTextView).text = "${playList.musicCount}首"
        return view
    }
}