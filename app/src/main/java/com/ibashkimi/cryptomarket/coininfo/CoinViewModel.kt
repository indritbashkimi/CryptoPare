package com.ibashkimi.cryptomarket.coininfo

import androidx.lifecycle.*
import com.ibashkimi.cryptomarket.data.UseCases
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey
import kotlinx.coroutines.flow.collect
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

    /*val history: LiveData<List<ChartPoint>?> = historyKey.asFlow()
        .map { key -> key?.let { UseCases.getHistory(coinId, it) } }
        .asLiveData()*/

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
