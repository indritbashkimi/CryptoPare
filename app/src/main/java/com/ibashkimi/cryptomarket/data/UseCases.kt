package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip

object UseCases {
    private val source = DataManager

    private val favoriteCoinsIds: Flow<Set<String>> = PreferenceHelper.favoriteCoinsFlow

    private val currency = PreferenceHelper.currency

    private val currencyFlow: Flow<String> = PreferenceHelper.currencyFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteCoins: Flow<List<Coin>?> = favoriteCoinsIds
        .zip(currencyFlow) { ids, currency -> Pair(ids, currency) }
        .flatMapLatest {
            // source.coins(ids, currency) loads the first 100 coins if ids is empty!
            if (it.first.isEmpty()) flowOf(emptyList())
            else source.coins(it.first, it.second)
        }

    fun coins(start: Int, limit: Int) = source.coins(start, limit, currency)

    suspend fun getCoins(start: Int, limit: Int) = source.getCoins(start, limit)

    fun coins(ids: Set<String>) = source.coins(ids, currency)

    fun coin(id: String) = source.coin(id, currency)

    suspend fun getCoin(id: String) = source.getCoin(id)

    suspend fun getHistory(id: String, interval: HistoryKey) =
        source.history(id, interval.key, currency)

    fun getHistoryKeys(): Set<HistoryKey> = source.getHistoryKeys()

    fun search(search: String, start: Int, limit: Int) =
        source.search(search, start, limit, currency)

    fun supportedCurrencies() = source.supportedCurrencies()
}