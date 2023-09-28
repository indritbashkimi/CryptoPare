package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.DataSource
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCoin
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCoinOrNull
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCoinsOrNull
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCurrency
import com.ibashkimi.cryptomarket.data.api.coincap.model.toDoubleOrNull
import com.ibashkimi.cryptomarket.data.api.coincap.model.toPointsOrNull
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

class CoinCapDataSource : DataSource {

    private val source = CoinCapApiImplementor()

    override fun coins(start: Int, limit: Int, currency: String): Flow<List<Coin>?> =
        source.getRate(currency)
            .map { it?.toDoubleOrNull() }
            .zip(source.coins(start, limit)) { rate, coins -> coins?.toCoinsOrNull(rate) }
            .flowOn(Dispatchers.IO)
            .catch { emit(null) }

    override suspend fun getCoins(start: Int, limit: Int): List<Coin>? {
        return try {
            source.getCoinList(start, limit)?.data?.map { it.toCoin() }
        } catch (e: Exception) {
            null
        }
    }

    override fun coins(ids: List<String>, currency: String): Flow<List<Coin>?> =
        source.getRate(currency)
            .map { it?.toDoubleOrNull() }
            .zip(source.coins(ids)) { rate, coins -> coins?.toCoinsOrNull(rate) }
            .flowOn(Dispatchers.IO)
            .catch { emit(null) }

    override suspend fun getCoins(ids: List<String>): List<Coin>? {
        return try {
            source.getCoinList(ids)?.data?.map { it.toCoin() }
        } catch (e: Exception) {
            null
        }
    }

    override fun coin(id: String, currency: String): Flow<Coin?> =
        source.getRate(currency)
            .map { it?.toDoubleOrNull() }
            .zip(source.getCoin(id)) { rate, coin -> coin?.toCoinOrNull(rate) }
            .flowOn(Dispatchers.IO)
            .catch { emit(null) }

    override suspend fun getCoin(id: String): Coin? {
        return try {
            source.getCoinInUsd(id)?.data?.toCoin()
        } catch (e: Exception) {
            null
        }
    }

    override fun history(
        id: String,
        interval: String,
        currency: String
    ): Flow<List<ChartPoint>?> =
        source.getRate(currency)
            .map { it?.toDoubleOrNull() }
            .zip(source.getHistory(id, interval)) { rate, history -> history?.toPointsOrNull(rate) }
            .flowOn(Dispatchers.IO)
            .catch { emit(null) }

    override fun search(
        search: String,
        start: Int,
        limit: Int,
        currency: String
    ): Flow<List<Coin>?> =
        source.getRate(currency)
            .map { it?.toDoubleOrNull() }
            .zip(source.search(search, start, limit)) { rate, coins -> coins?.toCoinsOrNull(rate) }
            .flowOn(Dispatchers.IO)
            .catch { emit(null) }

    override fun getHistoryKeys() = source.getHistoryKeys()

    override fun supportedCurrencies(): Flow<List<Currency>?> =
        source.getRates()
            .map { ratesResult -> ratesResult?.data?.map { it.toCurrency() } }
            .flowOn(Dispatchers.IO)
            .catch { emit(null) }
}