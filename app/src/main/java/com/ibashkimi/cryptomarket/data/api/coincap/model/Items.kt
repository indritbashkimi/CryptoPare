package com.ibashkimi.cryptomarket.data.api.coincap.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AssetItem(val data: List<Asset>, val timestamp: Long)

@JsonClass(generateAdapter = true)
data class CoinItem(val data: Asset, val timestamp: Long)

@JsonClass(generateAdapter = true)
data class HistoryItem(val data: List<HistoryPoint>, val timestamp: Long)

@JsonClass(generateAdapter = true)
data class Asset(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String?,
    val maxSupply: String?,
    val marketCapUsd: String?,
    val volumeUsd24Hr: String?,
    val priceUsd: String?,
    val changePercent24Hr: String?,
    val vwap24Hr: String?
)

@JsonClass(generateAdapter = true)
data class HistoryPoint(val priceUsd: String, val time: Long)

@JsonClass(generateAdapter = true)
data class RatesResult(val data: List<RateItem>, val timestamp: Long)

@JsonClass(generateAdapter = true)
data class RateResult(val data: RateItem, val timestamp: Long)

@JsonClass(generateAdapter = true)
data class RateItem(
    val id: String,
    val symbol: String,
    val currencySymbol: String?,
    val rateUsd: String,
    val type: String// type of currency - fiat or crypto
)