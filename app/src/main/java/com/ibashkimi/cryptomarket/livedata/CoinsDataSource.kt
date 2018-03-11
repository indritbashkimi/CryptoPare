package com.ibashkimi.cryptomarket.livedata

import android.arch.paging.ItemKeyedDataSource
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper


class CoinsDataSource : ItemKeyedDataSource<Int, Coin>() {

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Coin>) {
        // Ignored
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Coin>) {
        DataManager.getCoins(0, params.requestedLoadSize,
                PreferenceHelper.currency,
                onSuccess = {
                    callback.onResult(it)
                },
                onFailure = {
                })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Coin>) {
        DataManager.getCoins(params.key, params.requestedLoadSize,
                PreferenceHelper.currency,
                onSuccess = {
                    callback.onResult(it)
                },
                onFailure = {

                })
    }

    override fun getKey(item: Coin): Int {
        return item.rank.toInt()
    }
}