package moe.lz233.meizugravity.design.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import moe.lz233.meizugravity.design.App
import moe.lz233.meizugravity.design.R
import moe.lz233.meizugravity.design.utils.ktx.toInt

class ChanSwitch @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatButton(context, attrs) {

    private var valueType = -1
    private var key: String?
    private var value: List<String>
    private var valueDefault: String
    private var defaultBoolean = false
    private var baseText: String?
    private var optionText: List<String>

    init {
        background = ContextCompat.getDrawable(getContext(), R.drawable.button_background)
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ChanSwitch)
        key = typedArray.getString(R.styleable.ChanSwitch_key)
        value = typedArray.getValueAndType().split('|')
        valueDefault = value[typedArray.getInteger(R.styleable.ChanSwitch_valueDefault, 0)]
        baseText = typedArray.getString(R.styleable.ChanSwitch_baseText)
        optionText = typedArray.getString(R.styleable.ChanSwitch_optionText).toString().split('|')
        refreshText()
        setOnClickListener {
            when (valueType) {
                0 -> App.editor.putBoolean(key, !App.sp.getBoolean(key, defaultBoolean)).commit()
                1 -> App.editor.putString(key, value[(value.indexOf(App.sp.getString(key, valueDefault) + 1)) % value.size]).commit()
                2 -> App.editor.putInt(key, value[(value.indexOf(App.sp.getInt(key, valueDefault.toInt()).toString()) + 1) % value.size].toInt()).commit()
            }
            refreshText()
        }
        typedArray.recycle()
    }

    private fun refreshText() {
        text = when (valueType) {
            0 -> "$baseText：${optionText[1 - App.sp.getBoolean(key, defaultBoolean).toInt()]}"
            2 -> "$baseText：${optionText[value.indexOf(App.sp.getInt(key, valueDefault.toInt()).toString())]}"
            else -> "$baseText：${optionText[value.indexOf(App.sp.getString(key, valueDefault))]}"
        }
    }

    private fun TypedArray.getValueAndType(): String = when {
        getBoolean(R.styleable.ChanSwitch_valueIsBoolean, false) -> {
            valueType = 0
            defaultBoolean = getBoolean(R.styleable.ChanSwitch_valueDefaultBoolean, false)
            ""
        }
        getString(R.styleable.ChanSwitch_valueString) != null -> {
            valueType = 1
            getString(R.styleable.ChanSwitch_valueString).toString()
        }
        getString(R.styleable.ChanSwitch_valueInteger) != null -> {
            valueType = 2
            getString(R.styleable.ChanSwitch_valueInteger).toString()
        }
        else -> {
            valueType = -1
            ""
        }
    }
}
