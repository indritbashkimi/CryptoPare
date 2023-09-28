package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PreferenceChangedLiveData(
    private val sharedPreferences: SharedPreferences,
    private val keys: List<String>
) : MutableLiveData<String?>(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onActive() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key in keys) {
            value = key
        }
    }
}

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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, changed: String?) {
        if (key == changed) {
            value = onKeyChanged(key)
        }
    }
}

fun preferencesChangedFlow(
    sharedPreferences: SharedPreferences,
    keys: List<String>,
    emitOnCreate: Boolean = false
): Flow<String?> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key in keys) {
            trySend(key)
        }
    }
    if (emitOnCreate) {
        trySend(keys.first())
    }
    sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    awaitClose {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
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


fun createPreferenceChangedLiveData(
    sharedPreferences: SharedPreferences,
    keys: List<String>
): LiveData<String?> {
    return PreferenceChangedLiveData(sharedPreferences, keys)
}

fun <T> createPreferenceLiveData(
    sharedPreferences: SharedPreferences,
    key: String,
    onChanged: (SharedPreferences, String?) -> T
): LiveData<T> {
    return PreferenceLiveData(sharedPreferences, key) {
        onChanged(sharedPreferences, it)
    }
}