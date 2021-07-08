package moe.lz233.meizugravity.cloudmusic.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityMainBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.ui.login.LoginActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam

class MainActivity : BaseActivity() {
    private val viewBuilding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        if (UserDao.isLogin) {
            launch {
                val accountInfoResponse = CloudMusicNetwork.getAccountInfo()
                LogUtil.d(accountInfoResponse.profile.avatarUrl)
                Glide.with(viewBuilding.avatarImageView)
                        .load(accountInfoResponse.profile.avatarUrl.adjustParam("100", "100"))
                        .circleCrop()
                        .into(viewBuilding.avatarImageView)
                viewBuilding.userNameTextview.text = accountInfoResponse.profile.nickName
            }
        } else {
            LoginActivity.actionStart(this)
        }
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
        fun actionStart(context: Context) = context.startActivity(Intent(context, MainActivity::class.java))
    }
}