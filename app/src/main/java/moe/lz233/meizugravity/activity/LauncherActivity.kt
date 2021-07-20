package moe.lz233.meizugravity.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import moe.lz233.meizugravity.R
import moe.lz233.meizugravity.adapter.LauncherAdapter
import moe.lz233.meizugravity.databinding.ActivityLauncherBinding
import moe.lz233.meizugravity.utils.AppsInfoUtil

class LauncherActivity : BaseActivity() {
    private val appList by lazy { AppsInfoUtil(this).appList }
    private val launcherAdapter by lazy { LauncherAdapter(this, appList) }
    private val viewBuilding by lazy { ActivityLauncherBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        LayoutInflater.from(this@LauncherActivity).inflate(R.layout.item_app, viewBuilding.launcherListView, false).run {
            viewBuilding.launcherListView.addHeaderView(this, null, false)
            viewBuilding.launcherListView.addFooterView(this, null, false)
        }
        viewBuilding.launcherListView.adapter = launcherAdapter
        viewBuilding.launcherListView.setOnItemClickListener { adapterView, view, position, id ->
            val appInfo = appList[position - 1]
            launchApp(appInfo.packageName)
        }
    }

    private fun launchApp(packageName: String) = this.startActivity((this.packageManager as PackageManager).getLaunchIntentForPackage(packageName))

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
        fun actionStart(context: Context) = context.startActivity(Intent(context, LauncherActivity::class.java))
    }
}