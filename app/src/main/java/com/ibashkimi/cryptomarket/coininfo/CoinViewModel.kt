package com.ibashkimi.cryptomarket.coininfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibashkimi.cryptomarket.livedata.CoinChartLiveData
import com.ibashkimi.cryptomarket.livedata.CoinLiveData

class CoinViewModel(coinId: String, chartInterval: String) : ViewModel() {

    val coin = CoinLiveData(coinId)

    val chartData = CoinChartLiveData(coinId, chartInterval)

    class Factory(private val coinId: String, private val chartInterval: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CoinViewModel(coinId, chartInterval) as T
        }

    }
}
