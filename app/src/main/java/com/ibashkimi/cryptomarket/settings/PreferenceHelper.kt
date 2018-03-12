package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ibashkimi.cryptomarket.App

object PreferenceHelper {

    const val KEY_CURRENCY = "currency"
    const val DEFAULT_CURRENCY = "USD"

    const val KEY_NIGHT_MODE = "night_mode"
    const val DEFAULT_NIGHT_MODE = "auto"

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance())

    var currency: String
        get() = sharedPreferences.getString(KEY_CURRENCY, DEFAULT_CURRENCY)
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENCY, value).apply()
        }

    var nightMode: String
        get() = sharedPreferences.getString(KEY_NIGHT_MODE, DEFAULT_NIGHT_MODE)
        set(value) {
            sharedPreferences.edit().putString(KEY_NIGHT_MODE, value).apply()
        }
}