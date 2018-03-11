package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ibashkimi.cryptomarket.App

object PreferenceHelper {

    const val KEY_CURRENCY = "currency"
    const val DEFAULT_CURRENCY = "USD"

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance())

    var currency: String
        get() = sharedPreferences.getString(KEY_CURRENCY, DEFAULT_CURRENCY)
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENCY, value).apply()
        }
}