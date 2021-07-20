package moe.lz233.meizugravity.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import moe.lz233.meizugravity.R
import moe.lz233.meizugravity.meta.AppInfo

class LauncherAdapter(private val activity: Activity, appList: List<AppInfo>) : ArrayAdapter<AppInfo>(activity, R.layout.item_app, appList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
                ?: LayoutInflater.from(activity).inflate(R.layout.item_app, parent, false)
        val appInfo = getItem(position)!!
        view.findViewById<TextView>(R.id.appNameTextView).text = appInfo.appName
        view.findViewById<TextView>(R.id.packageNameTextView).text = appInfo.packageName
        return view
    }
}