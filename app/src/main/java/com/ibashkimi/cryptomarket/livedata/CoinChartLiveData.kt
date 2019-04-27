package com.ibashkimi.cryptomarket.livedata

import androidx.lifecycle.LiveData
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.ChartPoint

class CoinChartLiveData(private var id: String, private var interval: String) : LiveData<List<ChartPoint>?>() {

    override fun onActive() {
        super.onActive()
        if (value == null) {
            refresh()
        }
    }

    fun setCoinId(id: String) {
        this.id = id
        refresh()
    }

    fun setInterval(interval: String) {
        this.interval = interval
        refresh()
    }

    fun refresh() {
        DataManager.getHistory(id, interval, "USD") {
            when (it) {
                is ApiResponse.Success -> value = it.result
                is ApiResponse.Failure -> value = null
            }
        }
    }
}