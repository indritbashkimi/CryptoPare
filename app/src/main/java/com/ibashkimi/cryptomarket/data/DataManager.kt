package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapDataSource
import com.ibashkimi.cryptomarket.data.api.coincap.model.HistoryPoint
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.SearchItem


object DataManager {

    private val source: DataSource = CoinCapDataSource()

    fun getCoins(start: Int, limit: Int, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        source.getCoins(start, limit, currency, onResponse)
    }

    fun getCoins(ids: List<String>, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        source.getCoins(ids, currency, onResponse)
    }

    fun getCoin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit) {
        source.getCoin(id, currency, onResponse)
    }

    fun getHistory(id: String, interval: String, currency: String, onResponse: (ApiResponse<List<ChartPoint>>) -> Unit) {
        return source.getHistory(id, interval, onResponse)
    }

    fun search(search: String, start: Int, limit: Int, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        source.search(search, start, limit, onResponse)
    }
}