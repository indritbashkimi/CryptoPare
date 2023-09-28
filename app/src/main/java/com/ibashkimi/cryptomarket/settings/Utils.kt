package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PreferenceLiveData<T>(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val loadFirst: Boolean = false,
    private val onKeyChanged: (String?) -> T
) : MutableLiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onActive() {
        if (loadFirst) {
            value = onKeyChanged(key)
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?,
        changed: String?
    ) {
        if (key == changed) {
            value = onKeyChanged(key)
        }
    }
}

fun <T> preferenceFlow(
    sharedPreferences: SharedPreferences,
    key: String,
    onKeyChanged: (String) -> T
): Flow<T> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changed ->
        if (key == changed) {
            trySend(onKeyChanged(key))
        }
    }
    trySend(onKeyChanged(key))
    sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    awaitClose {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}

fun <T> PreferenceHelper.preferenceFlow(
    key: String,
    onKeyChanged: (String) -> T
): Flow<T> =
    preferenceFlow(sharedPreferences, key, onKeyChanged)
