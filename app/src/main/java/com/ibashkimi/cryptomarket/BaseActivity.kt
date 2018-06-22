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

    protected fun applyNightMode(nightMode: String) {
        when (nightMode) {
            "auto" -> delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
            "night" -> delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> throw IllegalArgumentException("Invalid night mode $nightMode.")
        }
    }
}