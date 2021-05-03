package moe.lz233.meizugravity.controller.fragment

import android.animation.LayoutTransition
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_touchpad.*
import moe.lz233.meizugravity.controller.R
import moe.lz233.meizugravity.controller.service.AdbService

class TouchPadFragment(private val viewPager2: ViewPager2, sharedPreferences: SharedPreferences, editor: SharedPreferences.Editor) : BaseFragment(sharedPreferences, editor) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_touchpad, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        touchPadFrameLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        touchPadView.viewPager2 = viewPager2
        runCommandExtendedFloatingActionButton.setOnClickListener { showRunCommandDialog() }
        centerFloatingActionButton.setOnClickListener { activity?.startService(Intent(activity, AdbService::class.java).putExtra("cmd", "input keyevent 66")) }
        backFloatingActionButton.setOnClickListener { activity?.startService(Intent(activity, AdbService::class.java).putExtra("cmd", "input keyevent 4")) }
        rotateFloatingActionButton.setOnClickListener {
            touchPadView.changeSize(touchPadView.height, touchPadView.width)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}