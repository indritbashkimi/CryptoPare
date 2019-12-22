package com.ibashkimi.cryptomarket.coininfo

import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayout

fun TabLayout.addTab(@StringRes text: Int, tag: Any) {
    addTab(this.newTab().apply {
        setText(text)
        this.tag = tag
    })
}

fun TabLayout.onTabSelected(onSelect: TabLayout.Tab.() -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) {
            // nothing
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            // nothing
        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            tab.onSelect()
        }
    })
}