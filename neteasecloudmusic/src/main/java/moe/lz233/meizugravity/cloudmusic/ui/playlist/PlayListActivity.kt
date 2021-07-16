package moe.lz233.meizugravity.cloudmusic.ui.playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityPlaylistBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.PlayList
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil

class PlayListActivity : BaseActivity() {
    private val playLists = mutableListOf<PlayList>()
    private val playlistAdapter by lazy { PlayListAdapter(this, playLists) }
    private val viewBuilding by lazy { ActivityPlaylistBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        initView()
        launch {
            val userPlaylistResponse = CloudMusicNetwork.getUserPlaylist(UserDao.id)
            playLists.addAll(userPlaylistResponse.playlists)
            playlistAdapter.notifyDataSetChanged()
            viewBuilding.playlistListView.setOnItemClickListener { adapterView, view, position, id ->
                val playList = playLists[position]
                LogUtil.toast(playList.name)
            }
        }
    }

    private fun initView() {
        viewBuilding.playlistListView.adapter = playlistAdapter
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
        fun actionStart(context: Context) = context.startActivity(Intent(context, PlayListActivity::class.java))
    }
}