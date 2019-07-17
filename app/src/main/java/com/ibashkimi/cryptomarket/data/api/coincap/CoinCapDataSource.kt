package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.DataSource
import com.ibashkimi.cryptomarket.data.api.coincap.model.*
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import kotlinx.coroutines.*

class CoinCapDataSource : DataSource {
    private val source = CoinCapApiImplementor()

    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getCoins(start: Int, limit: Int, currency: String): List<Coin>? {
        val coinsDeferred = CompletableDeferred<AssetItem?>()
        scope.launch {
            coinsDeferred.complete(source.getCoins(start, limit))
        }
        val rate = getRate(currency).await()?.rateUsd?.toDouble()
        val coins = coinsDeferred.await()

        return if (rate != null && coins != null) {
            coins.toCoinList(rate)
        } else {
            null
        }
    }

    override suspend fun getCoins(ids: List<String>, currency: String): List<Coin>? {
        val rateDeferred = getRate(currency)
        val coinsDeferred = scope.async {
            source.getCoins(ids)
        }
        /*val coinsDeferred = deferredCall {
            source.getCoins(ids)
        }*/

        //joinAll(rateDeferred, coinsDeferred)
        val rate = rateDeferred.await()?.rateUsd?.toDouble()
        val coins = coinsDeferred.await()

        return if (rate != null && coins != null) {
            coins.toCoinList(rate)
        } else {
            null
        }
    }

    override suspend fun getCoin(id: String, currency: String): Coin? {
        val rateDeferred = getRate(currency)
        val coinDeferred = deferredCall {
            source.getCoin(id)
        }

        val rate = rateDeferred.await()?.rateUsd?.toDouble()
        val coin = coinDeferred.await()

        return if (rate != null && coin != null) {
            coin.toCoin(rate)
        } else {
            null
        }
    }

    override suspend fun getHistory(id: String, interval: String, currency: String): List<ChartPoint>? {
        val rateDeferred = getRate(currency)
        val coinHistoryDeferred = deferredCall {
            source.getHistory(id, interval)
        }

        val rate = rateDeferred.await()?.rateUsd?.toDouble()
        val history = coinHistoryDeferred.await()

        return if (rate != null && history != null) {
            history.toChartPointList(rate)
        } else {
            null
        }
    }

    override suspend fun search(search: String, start: Int, limit: Int, currency: String): List<Coin>? {
        val rateDeferred = getRate(currency)
        val coinsDeferred = deferredCall {
            source.search(search, start, limit)
        }

        val rate = rateDeferred.await()?.rateUsd?.toDouble()
        val coins = coinsDeferred.await()

        return if (rate != null && coins != null) {
            coins.toCoinList(rate)
        } else {
            null
        }
    }

    override fun getHistoryKeys() = source.getHistoryKeys()

    private suspend fun getRate(currency: String): CompletableDeferred<RateItem?> {
        return deferredCall {
            source.getRate(currency)?.data
        }
    }

    private suspend fun <T> deferredCall(call: suspend () -> T): CompletableDeferred<T?> {
        val deferred = CompletableDeferred<T?>()
        scope.launch {
            deferred.complete(call())
        }
        return deferred
    }

}


