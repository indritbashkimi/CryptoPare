package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.api.coincap.model.*
import com.ibashkimi.cryptomarket.model.ChartInterval
import com.ibashkimi.cryptomarket.model.historyKeysOf
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

    suspend fun getCoins(start: Int, limit: Int): AssetItem? {
        return try {
            coinCapApi.getCoins(start, limit).body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCoins(ids: List<String>): AssetItem? {
        val idsRep = StringBuilder()
        for (i in ids.indices) {
            idsRep.append(ids[i])
            if (i < ids.size - 1)
                idsRep.append(',')
        }

        return try {
            coinCapApi.getCoins(idsRep.toString()).body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCoin(id: String): CoinItem? {
        return try {
            coinCapApi.getCoin(id).body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getHistory(id: String, interval: String): HistoryItem? {
        return try {
            coinCapApi.getCoinHistory(id, interval).body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun search(search: String, start: Int, limit: Int): AssetItem? {
        return try {
            coinCapApi.search(search, start, limit).body()
        } catch (e: Exception) {
            null
        }
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

    suspend fun getRates(): RatesResult? {
        return try {
            coinCapApi.getRates().body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRate(id: String): RateResult? {
        return try {
            coinCapApi.getRate(id).body()
        } catch (e: Exception) {
            null
        }
    }
}