package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey

interface DataSource {

    fun getCoins(start: Int, limit: Int, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit)

    fun getCoins(ids: List<String>, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit)

    fun getCoin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit)

    fun getHistory(id: String, interval: String, onResponse: (ApiResponse<List<ChartPoint>>) -> Unit)

    fun getHistoryKeys(): Set<HistoryKey>

    fun search(search: String, start: Int, limit: Int, onResponse: (ApiResponse<List<Coin>>) -> Unit)
}