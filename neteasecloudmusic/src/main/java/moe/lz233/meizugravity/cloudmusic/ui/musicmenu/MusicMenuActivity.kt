package moe.lz233.meizugravity.cloudmusic.ui.musicmenu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityMusicMenuBinding
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork.removeMusicFromPlaylist
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.ui.playlist.PlayListActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ViewPager2Util

class MusicMenuActivity : BaseActivity() {
    private val count = 4
    private val viewBuilding by lazy { ActivityMusicMenuBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        viewBuilding.menuViewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewBuilding.menuViewPager2.isUserInputEnabled = false
        viewBuilding.menuViewPager2.offscreenPageLimit = 3
        viewBuilding.menuViewPager2.adapter = ViewPagerAdapter()
        (viewBuilding.menuViewPager2.getChildAt(0) as RecyclerView).apply {
            setPadding(0, 25, 0, 25)
            clipToPadding = false
        }
        Glide.with(viewBuilding.coverImageView)
                .load(intent.getStringExtra("coverImageUrl"))
                .into(viewBuilding.coverImageView)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (viewBuilding.menuViewPager2.currentItem == 0)
                    ViewPager2Util.setCurrentItem(viewBuilding.menuViewPager2, 0, 100, 50)
                else
                    ViewPager2Util.setCurrentItem(viewBuilding.menuViewPager2, viewBuilding.menuViewPager2.currentItem - 1, 100, 50)
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (viewBuilding.menuViewPager2.currentItem == count - 1)
                    ViewPager2Util.setCurrentItem(viewBuilding.menuViewPager2, count - 1, 100, 50)
                else
                    ViewPager2Util.setCurrentItem(viewBuilding.menuViewPager2, viewBuilding.menuViewPager2.currentItem + 1, 100, 50)
            }
            KeyEvent.KEYCODE_ENTER -> {
                when (viewBuilding.menuViewPager2.currentItem) {
                    0 -> {

                    }
                    1 -> {
                    }
                    2 -> {
                        PlayListActivity.actionStartForAddMusicToPlayList(this, intent.getLongExtra("musicId", 0))
                        finish()
                    }
                    3 -> launch {
                        val modifyPlayListTracksResponse = intent.getLongExtra("musicId", 0)
                                .removeMusicFromPlaylist(intent.getLongExtra("playlistId", 0))
                        if (modifyPlayListTracksResponse.data.code == 200) LogUtil.toast("操作成功")
                        else LogUtil.toast(modifyPlayListTracksResponse.data.message)
                        finish()
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun actionStart(context: Context, coverImageUrl: String, playlistId: Long, musicId: Long, artistsName: String, albumName: String) =
                context.startActivity(Intent(context, MusicMenuActivity::class.java)
                        .apply {
                            putExtra("coverImageUrl", coverImageUrl)
                            putExtra("playlistId", playlistId)
                            putExtra("musicId", musicId)
                            putExtra("artistsName", artistsName)
                            putExtra("albumName", albumName)
                        })
    }

    inner class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.musicMenuItemTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_music_menu, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = when (position) {
                0 -> "歌手：${intent.getStringExtra("artistsName")}"
                1 -> "专辑：${intent.getStringExtra("albumName")}"
                2 -> "添加到歌单"
                3 -> "从歌单删除"
                else -> "not completed"
            }
        }

        override fun getItemCount() = count
    }
}