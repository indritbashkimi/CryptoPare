package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.api.coincap.model.*
import com.ibashkimi.cryptomarket.model.ChartInterval
import com.ibashkimi.cryptomarket.model.historyKeysOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CoinCapApiImplementor {

    private val coinCapApi: CoinCapApi by lazy {
        Retrofit.Builder()
            .baseUrl(CoinCapApi.ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CoinCapApi::class.java)
    }

    fun coins(start: Int, limit: Int): Flow<AssetItem?> = flow {
        emit(coinCapApi.getCoins(start, limit).body())
    }

    suspend fun getCoinList(start: Int, limit: Int): AssetItem? =
        coinCapApi.getCoins(start, limit).body()

    suspend fun getCoinList(ids: List<String>): AssetItem? =
        coinCapApi.getCoins(ids.reduce()).body()

    private fun List<String>.reduce(): String {
        val ids = this
        val idsRep = StringBuilder()
        for (i in ids.indices) {
            idsRep.append(ids[i])
            if (i < ids.size - 1)
                idsRep.append(',')
        }
        return idsRep.toString()
    }

    fun coins(ids: List<String>): Flow<AssetItem?> = flow {
        emit(coinCapApi.getCoins(ids.reduce()).body())
    }

    fun getCoin(id: String): Flow<CoinItem?> = flow {
        emit(coinCapApi.getCoin(id).body())
    }

    suspend fun getCoinInUsd(id: String): CoinItem? =
        coinCapApi.getCoin(id).body()

    fun getHistory(id: String, interval: String): Flow<HistoryItem?> = flow {
        emit(coinCapApi.getCoinHistory(id, interval).body())
    }

    fun search(search: String, start: Int, limit: Int): Flow<AssetItem?> = flow {
        emit(coinCapApi.search(search, start, limit).body())
    }

    fun getHistoryKeys() = historyKeysOf(
        ChartInterval.DAY to "m1",
        ChartInterval.WEEK to "m15",
        ChartInterval.WEEK2 to "m30",
        ChartInterval.MONTH to "h1",
        ChartInterval.MONTH2 to "h2",
        ChartInterval.MONTH6 to "h6",
        ChartInterval.YEAR to "h12",
        ChartInterval.YEAR2 to "d1"
    )

    fun getRates(): Flow<RatesResult?> = flow { emit(coinCapApi.getRates().body()) }

    fun getRate(id: String): Flow<RateResult?> =
        flow { emit(coinCapApi.getRate(id).body()) }
}