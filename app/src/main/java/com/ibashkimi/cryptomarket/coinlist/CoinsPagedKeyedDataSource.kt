package com.ibashkimi.cryptomarket.coinlist

import androidx.paging.PageKeyedDataSource
import com.ibashkimi.cryptomarket.data.UseCases
import com.ibashkimi.cryptomarket.model.Coin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CoinsPagedKeyedDataSource : PageKeyedDataSource<Int, Coin>() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        // Ignored
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Coin>
    ) {
        scope.launch {
            UseCases.coins(
                0,
                params.requestedLoadSize
            ).collect {
                callback.onResult(
                    it ?: emptyList(), null, params.requestedLoadSize
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        scope.launch {
            UseCases.coins(
                params.key,
                params.requestedLoadSize
            ).collect {
                callback.onResult(
                    it ?: emptyList(),
                    if (it?.size == params.requestedLoadSize)
                        it.last().rank.toInt() else null
                )
            }
        }
    }

    override fun invalidate() {
        android.util.Log.d("CoinsDataSource", "invalidate")
        super.invalidate()
        scope.cancel()
    }
}