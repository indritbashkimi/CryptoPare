package com.ibashkimi.cryptomarket.coininfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ibashkimi.cryptomarket.data.UseCases
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CoinViewModel : ViewModel() {

    val coinId = MutableLiveData<String?>()

    val coin: LiveData<Coin?> = coinId.asFlow()
        .map { id -> id?.let { UseCases.getCoin(it) } }
        .asLiveData()

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

    val historyKeys = UseCases.getHistoryKeys()

    fun refresh() {
        coinId.value = coinId.value
        historyKey.value = historyKey.value
    }

    private fun refreshChart(coinId: String, interval: HistoryKey) {
        viewModelScope.launch {
            UseCases.getHistory(coinId, interval).collect {
                history.value = it
            }
        }
    }
}
