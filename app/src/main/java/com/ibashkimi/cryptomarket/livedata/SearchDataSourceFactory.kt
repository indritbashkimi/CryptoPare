package com.ibashkimi.cryptomarket.livedata

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ibashkimi.cryptomarket.model.Coin

class SearchDataSourceFactory(private val search: String) : DataSource.Factory<Int, Coin>() {

    private lateinit var searchDataSource: SearchDataSource

    private val coinsDataSourceLiveData = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, Coin> {
        searchDataSource = SearchDataSource(search)
        coinsDataSourceLiveData.postValue(searchDataSource)
        return searchDataSource
    }

    fun invalidate() {
        searchDataSource.invalidate()
    }
}