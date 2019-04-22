package com.ibashkimi.cryptomarket.livedata

import androidx.lifecycle.LiveData
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.ChartPoint

class CoinChartLiveData(private val id: String) : LiveData<List<ChartPoint>?>() {

    init {
        refresh()
    }

    fun refresh() {
        DataManager.getHistory(id, "m1", "USD") {
            when (it) {
                is ApiResponse.Success -> value = it.result
                is ApiResponse.Failure -> value = null
            }
        }
    }
}