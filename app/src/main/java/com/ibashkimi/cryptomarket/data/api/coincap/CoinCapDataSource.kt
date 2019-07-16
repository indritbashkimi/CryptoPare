package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.DataSource
import com.ibashkimi.cryptomarket.data.api.coincap.model.toChartPointList
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCoin
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCoinList
import com.ibashkimi.cryptomarket.model.ChartInterval
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.historyKeysOf
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CoinCapDataSource : DataSource {

    private val coinCapApi: CoinCapApi by lazy {
        Retrofit.Builder()
                .baseUrl(CoinCapApi.ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(CoinCapApi::class.java)
    }

    override suspend fun getCoins(start: Int, limit: Int, currency: String): List<Coin>? {
        return coinCapApi.getCoins(start, limit).body()?.toCoinList()
    }

    override suspend fun getCoins(ids: List<String>, currency: String): List<Coin>? {
        val idsRep = StringBuilder()
        for (i in ids.indices) {
            idsRep.append(ids[i])
            if (i < ids.size - 1)
                idsRep.append(',')
        }

        return coinCapApi.getCoins(idsRep.toString()).body()?.toCoinList()
    }

    override suspend fun getCoin(id: String, currency: String): Coin? {
        return coinCapApi.getCoin(id).body()?.toCoin()
    }

    override suspend fun getHistory(id: String, interval: String): List<ChartPoint>? {
        return coinCapApi.getCoinHistory(id, interval).body()?.toChartPointList()
    }

    override suspend fun search(search: String, start: Int, limit: Int): List<Coin>? {
        return coinCapApi.search(search, start, limit).body()?.toCoinList()
    }

    override fun getHistoryKeys() = historyKeysOf(
            ChartInterval.DAY to "m1",
            ChartInterval.WEEK to "m15",
            ChartInterval.WEEK2 to "m30",
            ChartInterval.MONTH to "h1",
            ChartInterval.MONTH2 to "h2",
            ChartInterval.MONTH6 to "h6",
            ChartInterval.YEAR to "h12",
            ChartInterval.YEAR2 to "d1"
    )
}