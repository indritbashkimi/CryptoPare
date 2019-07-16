package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapDataSource
import com.ibashkimi.cryptomarket.model.ChartInterval
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey


object DataManager {

    private val source: DataSource = CoinCapDataSource()

    fun getCoins(start: Int, limit: Int, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        source.getCoins(start, limit, currency, onResponse)
    }

    fun getCoins(ids: Set<String>, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        source.getCoins(ids.toList(), currency, onResponse)
    }

    fun getCoin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit) {
        source.getCoin(id, currency, onResponse)
    }

    fun getHistory(id: String, interval: String, currency: String, onResponse: (ApiResponse<List<ChartPoint>>) -> Unit) {
        return source.getHistory(id, interval, onResponse)
    }

    fun getHistoryKeys(): Set<HistoryKey> {
        return source.getHistoryKeys()
    }

    fun search(search: String, start: Int, limit: Int, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        source.search(search, start, limit, onResponse)
    }
}