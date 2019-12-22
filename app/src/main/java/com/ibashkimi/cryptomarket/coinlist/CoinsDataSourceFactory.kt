package com.ibashkimi.cryptomarket.coinlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ibashkimi.cryptomarket.model.Coin


class CoinsDataSourceFactory : DataSource.Factory<Int, Coin>() {

    private lateinit var coinsDataSource: CoinsPagedKeyedDataSource

    private val coinsDataSourceLiveData = MutableLiveData<CoinsPagedKeyedDataSource>()

    override fun create(): DataSource<Int, Coin> {
        coinsDataSource = CoinsPagedKeyedDataSource()
        coinsDataSourceLiveData.postValue(coinsDataSource)
        return coinsDataSource
    }

}