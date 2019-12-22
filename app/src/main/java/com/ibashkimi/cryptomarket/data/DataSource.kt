package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.Currency
import com.ibashkimi.cryptomarket.model.HistoryKey
import kotlinx.coroutines.flow.Flow

interface DataSource {

    suspend fun getCoin(id: String): Coin?

    suspend fun getCoins(start: Int, limit: Int): List<Coin>?

    suspend fun getCoins(ids: List<String>): List<Coin>?

    fun getHistoryKeys(): Set<HistoryKey>

    fun coin(id: String, currency: String): Flow<Coin?>

    fun coins(start: Int, limit: Int, currency: String): Flow<List<Coin>?>

    fun coins(ids: List<String>, currency: String): Flow<List<Coin>?>

    fun history(id: String, interval: String, currency: String): Flow<List<ChartPoint>?>

    fun search(search: String, start: Int, limit: Int, currency: String): Flow<List<Coin>?>

    fun supportedCurrencies(): Flow<List<Currency>?>
}