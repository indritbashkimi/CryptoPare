package com.ibashkimi.cryptomarket.coininfo

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey
import com.ibashkimi.cryptomarket.settings.PreferenceHelper

class CoinViewModel : ViewModel() {

    val coinId = MutableLiveData<String?>()

    val coin = MediatorLiveData<Coin?>().apply {
        addSource(coinId) {
            if (it == null) {
                value = null
            } else {
                refreshCoin(it)
            }
        }
    }

    val historyKey = MutableLiveData<HistoryKey>()

    val history = MediatorLiveData<List<ChartPoint>?>().apply {
        addSource(historyKey) {
            val coinId = coinId.value
            if (it == null || coinId == null) {
                value = null
            } else {
                refreshChart(coinId, it)
            }
        }
    }

    val historyKeys = DataManager.getHistoryKeys()

    fun refresh() {
        coinId.value = coinId.value
        historyKey.value = historyKey.value
    }

    private fun refreshCoin(coinId: String) {
        DataManager.getCoin(coinId, PreferenceHelper.currency,
                onResponse = {
                    when (it) {
                        is ApiResponse.Failure -> coin.value = null
                        is ApiResponse.Success -> coin.value = it.result
                    }
                })
    }

    private fun refreshChart(coinId: String, interval: HistoryKey) {
        DataManager.getHistory(coinId, interval.key, "USD") {
            when (it) {
                is ApiResponse.Success -> history.value = it.result
                is ApiResponse.Failure -> history.value = null
            }
        }
    }
}
