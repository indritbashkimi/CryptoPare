package com.ibashkimi.cryptomarket.search

import androidx.paging.PageKeyedDataSource
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper

class SearchDataSource(val search: String) : PageKeyedDataSource<Int, Coin>() {

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        // Ignored
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Coin>) {
        DataManager.search(search, 0, params.requestedLoadSize) {
            when (it) {
                is ApiResponse.Failure -> {
                }
                is ApiResponse.Success -> {
                    callback.onResult(it.result, null, params.requestedLoadSize)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        DataManager.getCoins(params.key, params.requestedLoadSize, PreferenceHelper.currency) {
            when (it) {
                is ApiResponse.Success -> callback.onResult(it.result,
                        if (it.result.size == params.requestedLoadSize)
                            it.result.last().rank.toInt() else null)
                is ApiResponse.Failure -> callback.onResult(emptyList(), null)
            }
        }
    }
}