package com.ibashkimi.cryptomarket.livedata

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.ibashkimi.cryptomarket.model.Coin


class FavoriteCoinsDataSourceFactory : DataSource.Factory<Int, Coin> {

    private lateinit var coinsDataSource: FavoriteCoinsDataSource

    private val coinsDataSourceLiveData = MutableLiveData<FavoriteCoinsDataSource>()

    override fun create(): DataSource<Int, Coin> {
        coinsDataSource = FavoriteCoinsDataSource()
        coinsDataSourceLiveData.postValue(coinsDataSource)
        return coinsDataSource
    }

    fun invalidate() {
        coinsDataSource.invalidate()
    }
}