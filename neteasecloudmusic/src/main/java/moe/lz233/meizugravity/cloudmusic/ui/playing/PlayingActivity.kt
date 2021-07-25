package moe.lz233.meizugravity.cloudmusic.ui.playing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.zhy.mediaplayer_exo.playermanager.MediaPlayerExoPlayMode
import com.zhy.mediaplayer_exo.playermanager.MediaSwitchTrackChange
import com.zhy.mediaplayer_exo.playermanager.PlaylistItem
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityPlayingBinding
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork.like
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.ui.playlist.PlayListActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ViewPager2Util
import moe.lz233.meizugravity.cloudmusic.utils.ktx.AudioManager
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam

class PlayingActivity : BaseActivity() {
    private val count = 9
    private val viewBuilding by lazy { ActivityPlayingBinding.inflate(layoutInflater) }
    private var isShowMenu = false
    private val handler = Handler(Looper.getMainLooper())
    private val adapter = ViewPagerAdapter()
    private val runnable = object : Runnable {
        override fun run() {
            viewBuilding.lrcView.updateTime(MediaManager.getCurrentPosition())
            handler.postDelayed(this, 400)
        }
    }

    private val runnable2 = Runnable {
        runOnUiThread {
            hideMenu()
        }
    }

    private val mediaTrackChangeListener = object : MediaSwitchTrackChange {
        override fun onTracksChange(playlistItem: PlaylistItem) {
            onMediaChange(playlistItem)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        viewBuilding.mainViewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewBuilding.mainViewPager2.isUserInputEnabled = false
        viewBuilding.mainViewPager2.offscreenPageLimit = 3
        viewBuilding.mainViewPager2.adapter = adapter
        (viewBuilding.mainViewPager2.getChildAt(0) as RecyclerView).apply {
            setPadding(0, 25, 0, 25)
            clipToPadding = false
        }
        if (MediaManager.playlistItemList.isNotEmpty()) onMediaChange()
        handler.post(runnable)
        MediaManager.addMediaSwitchChange(mediaTrackChangeListener)
    }

    private fun onMediaChange(playlistItem: PlaylistItem = MediaManager.getCurrentMedia()) {
        adapter.notifyDataSetChanged()
        Glide.with(viewBuilding.coverImageView)
                .load(playlistItem.coverUrl?.adjustParam("150", "150"))
                .into(viewBuilding.coverImageView)
        launch {
            viewBuilding.lrcView.loadLrc("")
            val songLyricResponse = CloudMusicNetwork.getSongLyric(MediaManager.currentId()!!.toLong())
            songLyricResponse.uncollected?.let {
                viewBuilding.lrcView.loadLrc("[00:00.000] ${MediaManager.getCurrentMediaName()}\n[00:00.000] 貌似没歌词")
                return@launch
            }
            songLyricResponse.noLyric?.let {
                viewBuilding.lrcView.loadLrc("[00:00.000] ${MediaManager.getCurrentMediaName()}\n[00:00.000] 似乎是个纯音乐")
                return@launch
            }
            if (songLyricResponse.translatedLyric.lyric == "")
                viewBuilding.lrcView.loadLrc(songLyricResponse.lyric.lyric)
            else
                viewBuilding.lrcView.loadLrc(songLyricResponse.lyric.lyric, songLyricResponse.translatedLyric.lyric)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (isShowMenu) {
                    reCreateCallback()
                    if (viewBuilding.mainViewPager2.currentItem == 0)
                        ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, 0, 100, 50)
                    else
                        ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, viewBuilding.mainViewPager2.currentItem - 1, 100, 50)
                } else {
                    AudioManager.adjustStreamVolume(AudioManager.VOLUME_UP)
                    LogUtil.toast("音量：${AudioManager.currentVolume}/${AudioManager.maxVolume}")
                }
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (isShowMenu) {
                    reCreateCallback()
                    if (viewBuilding.mainViewPager2.currentItem == count - 1)
                        ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, count - 1, 100, 50)
                    else
                        ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, viewBuilding.mainViewPager2.currentItem + 1, 100, 50)
                } else {
                    AudioManager.adjustStreamVolume(AudioManager.VOLUME_DOWN)
                    LogUtil.toast("音量：${AudioManager.currentVolume}/${AudioManager.maxVolume}")
                }
            }
            KeyEvent.KEYCODE_ENTER -> {
                if (isShowMenu) {
                    hideMenu()
                    handler.removeCallbacks(runnable2)
                    when (viewBuilding.mainViewPager2.currentItem) {
                        0 -> {
                        }
                        1 -> MediaManager.playOrPause()
                        2 -> launch {
                            val likeResponse = MediaManager.currentId()!!.toLong().like()
                            if (likeResponse.code == 200) LogUtil.toast("操作成功")
                        }
                        3 -> PlayListActivity.actionStartForAddMusicToPlayList(this, MediaManager.currentId()!!.toLong())
                        4 -> MediaManager.playLast()
                        5 -> MediaManager.playNext()
                        6 -> when (MediaManager.getCurrentPlayMode()) {
                            MediaPlayerExoPlayMode.MEDIA_LIST_LOOP -> MediaManager.switchPlayMode(MediaPlayerExoPlayMode.MEDIA_ALONE_LOOP)
                            MediaPlayerExoPlayMode.MEDIA_ALONE_LOOP -> MediaManager.switchPlayMode(MediaPlayerExoPlayMode.MEDIA_LSIT_RANDOM)
                            MediaPlayerExoPlayMode.MEDIA_LSIT_RANDOM -> MediaManager.switchPlayMode(MediaPlayerExoPlayMode.MEDIA_LIST_LOOP)
                        }
                        7 -> LogUtil.toast(MediaManager.getCurrentMediaArtistName())
                        8 -> LogUtil.toast(MediaManager.getCurrentMediaAlbumName())
                    }
                } else {
                    showMenu()
                    reCreateCallback()
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showMenu() {
        adapter.notifyDataSetChanged()
        viewBuilding.lrcView.visibility = View.GONE
        viewBuilding.mainViewPager2.visibility = View.VISIBLE
        viewBuilding.mainViewPager2.setCurrentItem(1, false)
        isShowMenu = true
    }

    private fun hideMenu() {
        viewBuilding.mainViewPager2.visibility = View.GONE
        viewBuilding.lrcView.visibility = View.VISIBLE
        isShowMenu = false
    }

    private fun reCreateCallback() {
        handler.removeCallbacks(runnable2)
        handler.postDelayed(runnable2, 3000)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        handler.removeCallbacks(runnable2)
        MediaManager.removeMediaSwitchChange(mediaTrackChangeListener)
        super.onDestroy()
    }

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, PlayingActivity::class.java))
    }

    inner class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.playingItemTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_playing, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = when (position) {
                0 -> MediaManager.getCurrentMediaName()
                1 -> if (MediaManager.isPlaying()) "暂停" else "播放"
                2 -> "喜欢"
                3 -> "收藏到歌单"
                4 -> "上一首"
                5 -> "下一首"
                6 -> when (MediaManager.getCurrentPlayMode()) {
                    MediaPlayerExoPlayMode.MEDIA_LIST_LOOP -> "列表循环"
                    MediaPlayerExoPlayMode.MEDIA_ALONE_LOOP -> "单曲循环"
                    MediaPlayerExoPlayMode.MEDIA_LSIT_RANDOM -> "列表随机"
                    else -> "未知"
                }
                7 -> "歌手：${MediaManager.getCurrentMediaArtistName()}"
                8 -> "专辑：${MediaManager.getCurrentMediaAlbumName()}"
                else -> "not completed"
            }
        }

        override fun getItemCount() = count
    }
}