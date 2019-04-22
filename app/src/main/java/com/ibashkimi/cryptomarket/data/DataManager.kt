package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapDataSource
import com.ibashkimi.cryptomarket.data.api.coincap.model.HistoryPoint
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.SearchItem


object DataManager {

    private val source: DataSource = CoinCapDataSource()

    fun loadSupportedCoins(onResponse: (ApiResponse<List<SearchItem>>) -> Unit) {
    }

    fun getCoins(start: Int, limit: Int, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        source.getCoins(start, limit, currency, onResponse)
    }

    fun getCoin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit) {
        source.getCoin(id, currency, onResponse)
    }

    fun getHistory(id: String, interval: String, currency: String, onResponse: (ApiResponse<List<ChartPoint>>) -> Unit) {
        return source.getHistory(id, interval, onResponse)
    }

    fun history(coinId: String, period: HistoryPeriod, onResponse: (ApiResponse<String>) -> Unit) {
        onResponse(ApiResponse.Failure(""))
    }

    fun favoriteCoins(onResponse: (ApiResponse<List<Coin>>) -> Unit) {

    }
}