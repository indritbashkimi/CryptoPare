package com.ibashkimi.cryptomarket.livedata

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.ibashkimi.cryptomarket.model.Coin


class CoinsViewModel : ViewModel() {

    private val pageSize = 100

    var coins: LiveData<PagedList<Coin>>

    private val sourceFactory: CoinsDataSourceFactory = CoinsDataSourceFactory()

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize)
                .setEnablePlaceholders(false)
                .build()
        coins = LivePagedListBuilder<Int, Coin>(sourceFactory, config).build()
    }

    fun refresh() {
        sourceFactory.invalidate()
    }
}