package com.ibashkimi.cryptomarket.livedata

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ibashkimi.cryptomarket.model.Coin


class FavoriteCoinsDataSourceFactory : DataSource.Factory<Int, Coin>() {

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