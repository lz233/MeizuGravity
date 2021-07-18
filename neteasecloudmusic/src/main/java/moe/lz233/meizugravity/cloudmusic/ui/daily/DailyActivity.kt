package moe.lz233.meizugravity.cloudmusic.ui.daily

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityDailyBinding
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam
import moe.lz233.meizugravity.cloudmusic.utils.ktx.setOnItemSelectedListener

class DailyActivity : BaseActivity() {
    private val musicList = mutableListOf<Music>()
    private val dailyAdapter by lazy { DailyAdapter(this, musicList) }
    private val viewBuilding by lazy { ActivityDailyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        LayoutInflater.from(this@DailyActivity).inflate(R.layout.item_music, viewBuilding.dailyListView, false).run {
            viewBuilding.dailyListView.addHeaderView(this, null, false)
            viewBuilding.dailyListView.addFooterView(this, null, false)
        }
        launch {
            val dailyRecommendationResponse = CloudMusicNetwork.getDailyRecommendation()
            musicList.addAll(dailyRecommendationResponse.data.songs)
            viewBuilding.dailyListView.adapter = dailyAdapter
            Glide.with(viewBuilding.coverImageView)
                    .load(musicList[0].cover.picUrl.adjustParam("150", "150"))
                    .into(viewBuilding.coverImageView)
            viewBuilding.dailyListView.setOnItemClickListener { adapterView, view, position, id ->
                val music = musicList[position - 1]
                LogUtil.toast(music.name)
            }
            viewBuilding.dailyListView.setOnItemSelectedListener { selected, parent, view, position, id ->
                if (selected) {
                    val music = musicList[position!! - 1]
                    Glide.with(viewBuilding.coverImageView)
                            .load(music.cover.picUrl.adjustParam("150", "150"))
                            .into(viewBuilding.coverImageView)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
            KeyEvent.KEYCODE_DPAD_UP -> {
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
            }
            KeyEvent.KEYCODE_ENTER -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, DailyActivity::class.java))
    }
}