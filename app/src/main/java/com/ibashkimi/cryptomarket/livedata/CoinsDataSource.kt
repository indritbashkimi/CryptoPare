package com.ibashkimi.cryptomarket.livedata

import android.arch.paging.PageKeyedDataSource
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper


class CoinsDataSource : PageKeyedDataSource<Int, Coin>() {
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        // Ignored
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Coin>) {
        DataManager.getCoins(0, params.requestedLoadSize,
                PreferenceHelper.currency,
                onSuccess = {
                    callback.onResult(it, null, params.requestedLoadSize)
                },
                onFailure = {
                })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        DataManager.getCoins(params.key, params.requestedLoadSize,
                PreferenceHelper.currency,
                onSuccess = {
                    callback.onResult(it,
                            if (it.size == params.requestedLoadSize) it.last().rank.toInt() else null)
                },
                onFailure = {

                })
    }
}