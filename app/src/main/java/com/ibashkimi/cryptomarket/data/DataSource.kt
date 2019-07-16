package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey

interface DataSource {

    suspend fun getCoins(start: Int, limit: Int, currency: String): List<Coin>?

    suspend fun getCoins(ids: List<String>, currency: String): List<Coin>?

    suspend fun getCoin(id: String, currency: String): Coin?

    suspend fun getHistory(id: String, interval: String): List<ChartPoint>?

    fun getHistoryKeys(): Set<HistoryKey>

    suspend fun search(search: String, start: Int, limit: Int): List<Coin>?
}