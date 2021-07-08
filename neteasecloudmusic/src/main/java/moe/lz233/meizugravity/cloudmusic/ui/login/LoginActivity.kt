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

    fun startLogin() {
        launch {
            val keyResponse = CloudMusicNetwork.getKey(System.currentTimeMillis())
            val qrResponse = CloudMusicNetwork.createQrCode(keyResponse.data.key, System.currentTimeMillis())
            viewBuilding.qrImageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap(qrResponse.data.qrUrl, 100, 100))
            LogUtil.d(keyResponse.data.key)
            //runOnUiThread { viewBuilding.qrImageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap(qrResponse.data.qrUrl,100,100)) }
            check@ while (true) {
                val checkResponse = CloudMusicNetwork.checkQrStatus(keyResponse.data.key, System.currentTimeMillis())
                LogUtil.d(checkResponse.code)
                LogUtil.d(checkResponse.cookie)
                when (checkResponse.code) {
                    800 -> {
                        LogUtil.toast(checkResponse.message)
                        startLogin()
                        break@check
                    }
                    803 -> {
                        val musicU = checkResponse.cookie.substring(checkResponse.cookie.indexOf("MUSIC_U=") + 8)
                        LogUtil.d(musicU.substring(0, musicU.indexOf(';')))
                        UserDao.cookie = musicU.substring(0, musicU.indexOf(';'))
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