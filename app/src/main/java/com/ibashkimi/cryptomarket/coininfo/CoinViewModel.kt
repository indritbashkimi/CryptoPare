package com.ibashkimi.cryptomarket.coininfo

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        viewModelScope.launch(Dispatchers.IO) {
            coin.postValue(DataManager.getCoin(coinId, PreferenceHelper.currency))
        }
    }

    private fun refreshChart(coinId: String, interval: HistoryKey) {
        viewModelScope.launch(Dispatchers.IO) {
            history.postValue(DataManager.getHistory(coinId, interval.key, "USD"))
        }
    }
}
