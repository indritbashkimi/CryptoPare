package com.ibashkimi.cryptomarket.coinlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ibashkimi.cryptomarket.model.Coin

class CoinsViewModel : ViewModel() {

    var coins: LiveData<PagedList<Coin>> = CoinsDataSourceFactory().toLiveData(100)
}