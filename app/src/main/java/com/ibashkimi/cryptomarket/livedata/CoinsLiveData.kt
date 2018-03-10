package com.ibashkimi.cryptomarket.livedata

import android.arch.lifecycle.LiveData
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin


class CoinsLiveData : LiveData<List<Coin>?>() {

    override fun onActive() {
        super.onActive()
        if (value == null)
            refresh()
    }

    fun refresh() {
        DataManager.getCoins(
                onSuccess = {
                    value = it
                },
                onFailure = {
                    value = null
                })
    }
}