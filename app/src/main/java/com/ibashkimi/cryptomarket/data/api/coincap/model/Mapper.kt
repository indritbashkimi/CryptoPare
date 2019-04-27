package com.ibashkimi.cryptomarket.data.api.coincap.model

import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin

fun AssetItem.toCoinList() : List<Coin> = data.map { it.toCoin() }

fun Asset.toCoin(): Coin {
    return Coin(
            id = id,
            rank = rank,
            symbol = symbol,
            name = name,
            price = priceUsd,
            percentChange24h = changePercent24Hr,
            maxSupply = maxSupply,
            availableSupply = supply,
            marketCap = marketCapUsd,
            volume24h = volumeUsd24Hr,
            averagePrice24h = vwap24Hr)
}

fun CoinItem.toCoin(): Coin = data.toCoin()

fun HistoryItem.toChartPointList() = data.map { it.toChartPoint() }

fun HistoryPoint.toChartPoint() = ChartPoint(priceUsd.toDouble(), time)