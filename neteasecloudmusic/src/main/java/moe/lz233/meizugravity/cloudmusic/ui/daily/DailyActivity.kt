package moe.lz233.meizugravity.cloudmusic.ui.daily

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityDailyBinding
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity

class DailyActivity : BaseActivity() {
    private val songList = mutableListOf<Music>()
    private val dailyAdapter by lazy { DailyAdapter(this, songList) }
    private val viewBuilding by lazy { ActivityDailyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        initView()
        launch {
            val dailyRecommendationResponse = CloudMusicNetwork.getDailyRecommendation()
            songList.addAll(dailyRecommendationResponse.data.songs)
            dailyAdapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        viewBuilding.dailyListView.adapter = dailyAdapter
        //viewBuilding.dailyRecyclerView.layoutManager = LinearLayoutManager(this)
        //viewBuilding.dailyRecyclerView.adapter = dailyAdapter
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (requestCode == RESULT_OK) {
                finish()
                actionStart(this)
            }
        }
    }

    companion object {
        fun actionStart(context: Context) = context.startActivity(Intent(context, DailyActivity::class.java))
    }
}