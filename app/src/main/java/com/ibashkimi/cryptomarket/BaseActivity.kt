package com.ibashkimi.cryptomarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ibashkimi.cryptomarket.settings.PreferenceHelper

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        applyNightMode(PreferenceHelper.nightMode)
        super.onCreate(savedInstanceState)
    }

    fun applyNightMode(nightMode: String) {
        delegate.localNightMode = when (nightMode) {
            "auto", "daynight" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "night" -> AppCompatDelegate.MODE_NIGHT_YES
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            else -> throw IllegalArgumentException("Invalid night mode $nightMode.")
        }
    }
}