package com.ibashkimi.cryptomarket.search

import androidx.paging.PageKeyedDataSource
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchDataSource(val search: String) : PageKeyedDataSource<Int, Coin>() {

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        // Ignored
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Coin>) {
        CoroutineScope(Dispatchers.IO).launch {
            val res = DataManager.getCoins(0, params.requestedLoadSize, PreferenceHelper.currency)
            //val nextKey = if (res != null) params.requestedLoadSize else null
            withContext(Dispatchers.Main) {
                callback.onResult(res
                        ?: emptyList(), null, params.requestedLoadSize) // todo what if returns less than requested? check this
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        CoroutineScope(Dispatchers.IO).launch {
            val res = DataManager.getCoins(params.key, params.requestedLoadSize, PreferenceHelper.currency)
            withContext(Dispatchers.Main) {
                callback.onResult(res ?: emptyList(),
                        if (res?.size == params.requestedLoadSize)
                            res.last().rank.toInt() else null)
            }
        }
    }
}