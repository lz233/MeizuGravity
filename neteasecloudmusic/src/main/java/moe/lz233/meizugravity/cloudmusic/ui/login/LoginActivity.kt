package moe.lz233.meizugravity.cloudmusic.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityLoginBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.QRCodeUtil


class LoginActivity : BaseActivity() {
    private val viewBuilding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        startLogin()
    }

    private fun startLogin() {
        launch {
            val keyResponse = CloudMusicNetwork.getKey(3)
            viewBuilding.qrImageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap("https://music.163.com/login?codekey=${keyResponse.key}", 100, 100))
            LogUtil.d(keyResponse.key)
            check@ while (true) {
                val checkResponse = CloudMusicNetwork.checkQrStatus(keyResponse.key, 3)
                LogUtil.d(checkResponse.code)
                when (checkResponse.code) {
                    800 -> {
                        LogUtil.toast(checkResponse.message)
                        startLogin()
                        break@check
                    }
                    803 -> {
                        val userStatusResponse = CloudMusicNetwork.getAccountInfo()
                        LogUtil.d(userStatusResponse)
                        UserDao.id = userStatusResponse.profile.userId
                        UserDao.type = userStatusResponse.profile.userType
                        UserDao.isLogin = true
                        setResult(RESULT_OK, null)
                        finish()
                        break@check
                    }
                }
                delay(3000)
            }
        }
    }

    companion object {
        fun actionStart(activity: Activity) = activity.startActivityForResult(Intent(activity, LoginActivity::class.java), 1)
    }
}