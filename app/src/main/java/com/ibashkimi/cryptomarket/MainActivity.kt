package com.ibashkimi.cryptomarket

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ibashkimi.cryptomarket.settings.PreferenceHelper


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            PreferenceHelper.apply {
                initIfFirstTimeAnd {
                    nightMode = "system_default"
                    currency = "united-states-dollar"
                    currencySymbol = "$"
                    currencyName = "USD"
                }
                // Apply theme before onCreate
                applyGlobalNightMode(nightMode)
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

fun applyGlobalNightMode(nightMode: String) {
    AppCompatDelegate.setDefaultNightMode(
        when (nightMode) {
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            "system_default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "battery_saver" -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            else -> throw IllegalArgumentException("Invalid night mode $nightMode.")
        }
    )
}

fun Activity.applyNightMode(nightMode: String) {
    applyGlobalNightMode(nightMode)
    recreate()
}