package com.ibashkimi.cryptomarket.livedata

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.ibashkimi.cryptomarket.model.Coin


class CoinsDataSourceFactory : DataSource.Factory<Int, Coin> {

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