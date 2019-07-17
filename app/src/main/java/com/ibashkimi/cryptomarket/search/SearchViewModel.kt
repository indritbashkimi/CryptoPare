package com.ibashkimi.cryptomarket.search

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ibashkimi.cryptomarket.model.Coin

class SearchViewModel : ViewModel() {

    val searchChanged = MediatorLiveData<LiveData<PagedList<Coin>>>()

    var searchLiveData: LiveData<PagedList<Coin>>? = null

    fun search(search: String) {
        searchLiveData?.let {
            searchChanged.removeSource(it)
        }

        val sourceFactory = SearchDataSourceFactory(search)
        val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE * 2)
                .setEnablePlaceholders(false)
                .build()
        searchLiveData = LivePagedListBuilder(sourceFactory, config).build()
        searchChanged.addSource(searchLiveData!!) {
            searchChanged.value = searchLiveData
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}