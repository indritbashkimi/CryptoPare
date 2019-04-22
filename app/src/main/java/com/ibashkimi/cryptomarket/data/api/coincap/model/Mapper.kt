package com.ibashkimi.cryptomarket.data.api.coincap.model

import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin

fun AssetItem.toCoinList() : List<Coin> {
    return data.map { it.toCoin() }
}

fun Asset.toCoin(): Coin {
    return Coin(
            id = id,
            rank = rank,
            symbol = symbol,
            name = name,
            price = priceUsd,
            percentChange24h = changePercent24Hr,
            maxSupply = maxSupply,
            availableSupply = supply)
}

fun CoinItem.toCoin(): Coin {
    return data.toCoin()
}

fun HistoryItem.toChartPointList(): List<ChartPoint> {
    return data.map { it.toChartPoint() }
}

fun HistoryPoint.toChartPoint(): ChartPoint {
    return ChartPoint(priceUsd.toDouble(), time)
}