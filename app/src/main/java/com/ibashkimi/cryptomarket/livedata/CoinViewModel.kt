package com.ibashkimi.cryptomarket.livedata

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class CoinViewModel(coinId: String) : ViewModel() {

    val coin = CoinLiveData(coinId)

    class Factory(private val coinId: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CoinViewModel(coinId) as T
        }

    }
}
