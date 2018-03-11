package com.ibashkimi.cryptomarket.livedata

import android.arch.lifecycle.LiveData
import android.content.SharedPreferences
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper


class CoinsLiveData : LiveData<List<Coin>?>(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onActive() {
        super.onActive()
        PreferenceHelper.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        if (value == null)
            refresh()
    }

    override fun onInactive() {
        super.onInactive()
        PreferenceHelper.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (PreferenceHelper.KEY_CURRENCY == key) {
            refresh()
        }
    }

    fun refresh() {
        DataManager.getCoins(PreferenceHelper.currency,
                onSuccess = {
                    value = it
                },
                onFailure = {
                    value = null
                })
    }
}