package moe.lz233.meizugravity.cloudmusic.utils.ktx

import android.view.View
import android.widget.AdapterView
import android.widget.ListView

fun ListView.setOnItemSelectedListener(block: (selected: Boolean, parent: AdapterView<*>?, view: View?, position: Int?, id: Long?) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            block(true, parent, view, position, id)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            block(false, null, null, null, null)
        }
    }
}