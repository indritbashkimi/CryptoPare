package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.coinlist.CoinsDataSourceFactory
import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapDataSource
import com.ibashkimi.cryptomarket.model.HistoryKey

object DataManager {

    private val source = CoinCapDataSource()

    fun coins(start: Int, limit: Int, currency: String) = source.coins(start, limit, currency)

    fun coins() = CoinsDataSourceFactory()

    suspend fun getCoins(start: Int, limit: Int) = source.getCoins(start, limit)

    suspend fun getCoins(ids: List<String>) = source.getCoins(ids)

    fun coins(ids: Set<String>, currency: String) = source.coins(ids.toList(), currency)

    fun coin(id: String, currency: String) = source.coin(id, currency)

    suspend fun getCoin(id: String) = source.getCoin(id)

    suspend fun history(id: String, interval: String, currency: String) =
        source.history(id, interval, currency)

    fun getHistoryKeys(): Set<HistoryKey> = source.getHistoryKeys()

    fun search(search: String, start: Int, limit: Int, currency: String) =
        source.search(search, start, limit, currency)

    fun supportedCurrencies() = source.supportedCurrencies()
}
