package com.ibashkimi.cryptomarket.data.api.coincap.model

import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.Currency

fun AssetItem.toCoinList(rateUsd: Double = 1.0): List<Coin> = data.map { it.toCoin(rateUsd) }

fun Asset.toCoin(rateUsd: Double = 1.0) = Coin(
    id = id,
    rank = rank,
    symbol = symbol,
    name = name,
    price = priceUsd?.toDouble()?.let { (it / rateUsd).toString() },
    percentChange24h = changePercent24Hr,
    maxSupply = maxSupply,
    availableSupply = supply,
    marketCap = marketCapUsd?.toDouble()?.let { (it / rateUsd).toString() },
    volume24h = volumeUsd24Hr,
    averagePrice24h = vwap24Hr
)

fun CoinItem.toCoin(rateUsd: Double = 1.0): Coin = data.toCoin(rateUsd)

fun HistoryItem.toChartPointList(rateUsd: Double = 1.0) = data.map { it.toChartPoint(rateUsd) }

fun HistoryItem.toPointsOrNull(rateUsd: Double?) =
    rateUsd?.let { rate -> data.map { it.toChartPoint(rate) } }

fun HistoryPoint.toChartPoint(rateUsd: Double = 1.0) =
    ChartPoint(priceUsd.toDouble().let { (it / rateUsd) }, time)

fun RateResult.toDoubleOrNull() = this.data.rateUsd.toDoubleOrNull()

fun RateItem.toCurrency() = Currency(id, symbol, currencySymbol, rateUsd, type)

fun AssetItem.toCoinsOrNull(rate: Double?) =
    if (rate == null) null else toCoinList(rate)

fun CoinItem.toCoinOrNull(rate: Double?) = if (rate == null) null else toCoin(rate)