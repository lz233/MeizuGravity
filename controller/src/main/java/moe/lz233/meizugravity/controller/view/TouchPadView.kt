package moe.lz233.meizugravity.controller.view

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import moe.lz233.meizugravity.controller.Application
import moe.lz233.meizugravity.controller.service.AdbService
import moe.lz233.meizugravity.controller.util.LogUtil
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

const val screenWidth = 320
const val screenHeight = 240

class TouchPadView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : MaterialCardView(context, attrs, defStyleAttr) {

    var sizeChanged = false
    var firstX = 0f
    var firstY = 0f
    lateinit var viewPager2: ViewPager2

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        LogUtil.d(event.action)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> run {
                LogUtil.d("MotionEvent.ACTION_DOWN")
                viewPager2.isUserInputEnabled = false
                firstX = event.x
                firstY = event.y
            }
            MotionEvent.ACTION_UP -> run {
                LogUtil.d("MotionEvent.ACTION_UP")
                viewPager2.isUserInputEnabled = true
                if (((event.x - firstX).absoluteValue < 1) or ((event.y - firstY).absoluteValue < 1)) {
                    if (measuredWidth > measuredHeight)
                        Application.context.startService(Intent(Application.context, AdbService::class.java).putExtra("cmd", "input tap ${screenWidth * (event.x / measuredWidth)} ${screenHeight * (event.y / measuredHeight)}"))
                    else
                        Application.context.startService(Intent(Application.context, AdbService::class.java).putExtra("cmd", "input tap ${screenHeight * (event.x / measuredWidth)} ${screenWidth * (event.y / measuredHeight)}"))
                } else {
                    if (measuredWidth > measuredHeight)
                        Application.context.startService(Intent(Application.context, AdbService::class.java).putExtra("cmd", "input swipe ${screenWidth * (firstX / measuredWidth)} ${screenHeight * (firstY / measuredHeight)} ${screenWidth * (event.x / measuredWidth)} ${screenHeight * (event.y / measuredHeight)}"))
                    else
                        Application.context.startService(Intent(Application.context, AdbService::class.java).putExtra("cmd", "input swipe ${screenHeight * (firstX / measuredWidth)} ${screenWidth * (firstY / measuredHeight)} ${screenHeight * (event.x / measuredWidth)} ${screenWidth * (event.y / measuredHeight)}"))
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!sizeChanged) changeSize(measuredWidth, (measuredWidth * 0.75).roundToInt())
        sizeChanged = true
    }

    fun changeSize(width: Int, height: Int) {
        layoutParams = layoutParams.apply {
            this.width = width
            this.height = height
        }
    }
}