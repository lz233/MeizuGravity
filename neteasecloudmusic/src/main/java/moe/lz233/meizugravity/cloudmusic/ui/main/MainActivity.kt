package moe.lz233.meizugravity.cloudmusic.ui.main

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
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityMainBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.ui.daily.DailyActivity
import moe.lz233.meizugravity.cloudmusic.ui.login.LoginActivity
import moe.lz233.meizugravity.cloudmusic.ui.playlist.PlayListActivity
import moe.lz233.meizugravity.cloudmusic.utils.ViewPager2Util
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam

class MainActivity : BaseActivity() {
    private val mainMenuList by lazy { listOf("正在播放", "每日推荐", "我的歌单", "关于") }
    private val viewBuilding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        initView()
        if (UserDao.isLogin) {
            launch {
                val accountInfoResponse = CloudMusicNetwork.getAccountInfo()
                Glide.with(viewBuilding.avatarImageView)
                        .load(accountInfoResponse.profile.avatarUrl.adjustParam("100", "100"))
                        .placeholder(R.drawable.ic_account)
                        .circleCrop()
                        .into(viewBuilding.avatarImageView)
                viewBuilding.userNameTextview.text = accountInfoResponse.profile.nickName
                UserDao.name = accountInfoResponse.profile.nickName
            }
        } else {
            LoginActivity.actionStart(this)
        }
    }

    private fun initView() {
        viewBuilding.mainViewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewBuilding.mainViewPager2.isUserInputEnabled = false
        viewBuilding.mainViewPager2.offscreenPageLimit = 3
        viewBuilding.mainViewPager2.adapter = ViewPagerAdapter()
        (viewBuilding.mainViewPager2.getChildAt(0) as RecyclerView).apply {
            setPadding(0, 25, 0, 25)
            clipToPadding = false
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
            KeyEvent.KEYCODE_ENTER -> {
                when (viewBuilding.mainViewPager2.currentItem) {
                    0 -> TODO()
                    1 -> DailyActivity.actionStart(this)
                    2 -> PlayListActivity.actionStart(this)
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) recreate()
        }
    }

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, MainActivity::class.java))
    }

    inner class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.mainItemTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = mainMenuList[position]
        }

        override fun getItemCount() = mainMenuList.size
    }
}