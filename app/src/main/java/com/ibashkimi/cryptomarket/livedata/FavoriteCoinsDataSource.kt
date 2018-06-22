package com.ibashkimi.cryptomarket.livedata

import androidx.paging.PageKeyedDataSource
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin

class FavoriteCoinsDataSource : PageKeyedDataSource<Int, Coin>() {
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        // Ignored
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Coin>) {
        DataManager.favoriteCoins {
            when (it) {
                is ApiResponse.Success -> {
                    callback.onResult(it.result.sortedBy { it.rank.toInt() }, null, params.requestedLoadSize)
                }
                is ApiResponse.Failure -> {

                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {

    }
}