package com.ibashkimi.cryptomarket.coinlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ibashkimi.cryptomarket.model.Coin


class CoinsDataSourceFactory : DataSource.Factory<Int, Coin>() {

    private lateinit var coinsDataSource: CoinsDataSource

    private val coinsDataSourceLiveData = MutableLiveData<CoinsDataSource>()

    override fun create(): DataSource<Int, Coin> {
        coinsDataSource = CoinsDataSource()
        coinsDataSourceLiveData.postValue(coinsDataSource)
        return coinsDataSource
    }

    fun invalidate() {
        coinsDataSource.invalidate()
    }
}