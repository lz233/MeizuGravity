package moe.lz233.meizugravity.cloudmusic.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityMainBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.ui.about.AboutActivity
import moe.lz233.meizugravity.cloudmusic.ui.daily.DailyActivity
import moe.lz233.meizugravity.cloudmusic.ui.login.LoginActivity
import moe.lz233.meizugravity.cloudmusic.ui.playing.PlayingActivity
import moe.lz233.meizugravity.cloudmusic.ui.playlist.PlayListActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ViewPager2Util
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam
import moe.lz233.meizugravity.cloudmusic.utils.ktx.toPlayListItem

class MainActivity : BaseActivity() {
    private val mainMenuList by lazy { listOf("正在播放", "每日签到", "每日推荐", "我的歌单", "关于") }
    private val viewBuilding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var cast = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        viewBuilding.mainViewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewBuilding.mainViewPager2.isUserInputEnabled = false
        viewBuilding.mainViewPager2.offscreenPageLimit = 3
        viewBuilding.mainViewPager2.adapter = ViewPagerAdapter()
        (viewBuilding.mainViewPager2.getChildAt(0) as RecyclerView).apply {
            setPadding(0, 25, 0, 25)
            clipToPadding = false
        }
        when (UserDao.isLogin) {
            true -> launch {
                try {
                    val accountInfoResponse = CloudMusicNetwork.getAccountInfo()
                    Glide.with(viewBuilding.avatarImageView)
                            .load(accountInfoResponse.profile.avatarUrl.adjustParam("100", "100"))
                            .placeholder(R.drawable.ic_account)
                            .circleCrop()
                            .into(viewBuilding.avatarImageView)
                    viewBuilding.userNameTextview.text = accountInfoResponse.profile.nickName
                    UserDao.name = accountInfoResponse.profile.nickName
                    UserDao.type = accountInfoResponse.profile.userType
                } catch (throwable: Throwable) {
                    LogUtil.e(throwable)
                    showDialog()
                }
            }
            false -> LoginActivity.actionStart(this)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (viewBuilding.mainViewPager2.currentItem == 0)
                    ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, 0, 100, 50)
                else
                    ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, viewBuilding.mainViewPager2.currentItem - 1, 100, 50)
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (viewBuilding.mainViewPager2.currentItem == mainMenuList.lastIndex)
                    ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, mainMenuList.lastIndex, 100, 50)
                else
                    ViewPager2Util.setCurrentItem(viewBuilding.mainViewPager2, viewBuilding.mainViewPager2.currentItem + 1, 100, 50)
            }
            KeyEvent.KEYCODE_ENTER -> if (event?.action == KeyEvent.ACTION_DOWN) {
                //event.startTracking()
                if (event.repeatCount == 0) {
                    cast = false
                    return false
                } else {
                    cast = true
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if (cast) {
                    when (viewBuilding.mainViewPager2.currentItem) {
                        0 -> launch {
                            LogUtil.toast("尝试进行魔法对接（")
                            val userPlaylistResponse = CloudMusicNetwork.getUserPlaylist(UserDao.id)
                            for (playList in userPlaylistResponse.playlists) {
                                if (playList.creator.userId == UserDao.id && playList.name == "Cast to Gravity") {
                                    val playlistDetailResponse = CloudMusicNetwork.getPlaylistDetail(playList.id)
                                    MediaManager.playlist(playlistDetailResponse.playlist.tracks.toPlayListItem(), 0)
                                    MediaManager.play()
                                    PlayingActivity.actionStart(this@MainActivity)
                                    break
                                }
                            }
                        }
                    }

                } else {
                    when (viewBuilding.mainViewPager2.currentItem) {
                        0 -> PlayingActivity.actionStart(this)
                        1 -> checkIn()
                        2 -> DailyActivity.actionStart(this)
                        3 -> PlayListActivity.actionStart(this)
                        4 -> AboutActivity.actionStart(this)
                    }
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun checkIn() {
        launch {
            try {
                val androidCheckInResponse = CloudMusicNetwork.checkIn(0)
                if (androidCheckInResponse.code == 200) LogUtil.toast("Android 端签到，获得 ${androidCheckInResponse.point} 经验")
                else LogUtil.toast(androidCheckInResponse.message!!)
            } catch (throwable: Throwable) {
                LogUtil.toast("Android 端重复签到")
            }
            try {
                val webCheckInResponse = CloudMusicNetwork.checkIn(1)
                if (webCheckInResponse.code == 200) LogUtil.toast("Web 端签到，获得 ${webCheckInResponse.point} 经验")
                else LogUtil.toast(webCheckInResponse.message!!)
            } catch (throwable: Throwable) {
                LogUtil.toast("Web 端重复签到")
            }
            if (UserDao.type == 4) {
                if (CloudMusicNetwork.musicianCheckIn().code != 200) return@launch
                val musicianTasksResponse = CloudMusicNetwork.getMusicianTasks()
                musicianTasksResponse.data.tasks.forEach {
                    it.status?.let { status ->
                        if ((status == 20) and (it.userMissionId != null)) {
                            val obtainTasksResponse =
                                    CloudMusicNetwork.obtainMusicianTask(it.userMissionId!!, it.period)
                            if (obtainTasksResponse.code == 200) LogUtil.toast("完成音乐人任务：${it.name}\n获得 ${it.rewardWorth} 云豆")
                        }
                    }
                }
            }
        }
    }

    private fun showDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_logout, null)
        val builder = AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK).apply {
            setView(view)
        }
        builder.show().apply {
            setCancelable(false)
            view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
                dismiss()
            }
            view.findViewById<Button>(R.id.retryButton).setOnClickListener {
                dismiss()
                recreate()
            }
            view.findViewById<Button>(R.id.logoutButton).setOnClickListener {
                UserDao.isLogin = false
                LoginActivity.actionStart(this@MainActivity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) recreate()
        }
    }

    companion object {
        fun actionStart(context: Context) =
                context.startActivity(Intent(context, MainActivity::class.java))
    }

    inner class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.mainItemTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = mainMenuList[position]
        }

        override fun getItemCount() = mainMenuList.size
    }
}