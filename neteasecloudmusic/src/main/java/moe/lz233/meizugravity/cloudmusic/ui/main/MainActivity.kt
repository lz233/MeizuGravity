package moe.lz233.meizugravity.cloudmusic.ui.main

import android.os.Bundle
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.ui.login.LoginActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!UserDao.isLogin) LoginActivity.actionStart(this)
    }
}