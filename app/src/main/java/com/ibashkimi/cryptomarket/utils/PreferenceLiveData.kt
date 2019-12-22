package com.ibashkimi.cryptomarket.utils

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

class PreferenceLiveData<T>(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val loadFirst: Boolean = true,
    private val onValueChanged: SharedPreferences.(String) -> T
) : MutableLiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onActive() {
        if (loadFirst) {
            value = sharedPreferences.onValueChanged(key)
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, changed: String) {
        if (key == changed) {
            value = sharedPreferences.onValueChanged(key)
        }
    }
}