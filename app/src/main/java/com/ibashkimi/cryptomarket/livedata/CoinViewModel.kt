package com.ibashkimi.cryptomarket.livedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CoinViewModel(coinId: String) : ViewModel() {

    val coin = CoinLiveData(coinId)

    val chartData = CoinChartLiveData(coinId)

    class Factory(private val coinId: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CoinViewModel(coinId) as T
        }

    }
}
