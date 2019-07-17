package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapDataSource
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey


object DataManager {

    private val source = CoinCapDataSource()

    suspend fun getCoins(start: Int, limit: Int, currency: String): List<Coin>? {
        return source.getCoins(start, limit, currency)
    }

    suspend fun getCoins(ids: Set<String>, currency: String): List<Coin>? {
        return source.getCoins(ids.toList(), currency)
    }

    suspend fun getCoin(id: String, currency: String): Coin? {
        return source.getCoin(id, currency)
    }

    suspend fun getHistory(id: String, interval: String, currency: String): List<ChartPoint>? {
        return source.getHistory(id, interval, currency)
    }

    fun getHistoryKeys(): Set<HistoryKey> {
        return source.getHistoryKeys()
    }

    suspend fun search(search: String, start: Int, limit: Int, currency: String): List<Coin>? {
        return source.search(search, start, limit, currency)
    }
}