package com.ibashkimi.cryptomarket.coinlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ibashkimi.cryptomarket.model.Coin


class CoinsViewModel : ViewModel() {

    private val pageSize = 100

    var coins: LiveData<PagedList<Coin>>

    private val sourceFactory: CoinsDataSourceFactory = CoinsDataSourceFactory()

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
        coins = LivePagedListBuilder(sourceFactory, config).build()
    }

    fun refresh() {
        sourceFactory.invalidate()
    }
}