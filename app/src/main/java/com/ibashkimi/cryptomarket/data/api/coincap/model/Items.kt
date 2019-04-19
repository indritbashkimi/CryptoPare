package com.ibashkimi.cryptomarket.data.api.coincap.model


data class AssetItem(val data: List<Asset>, val timestamp: Long)


data class HistoryItem(val data: List<HistoryData>)


data class Asset(
        val id: String,
        val rank: String,
        val symbol: String,
        val name: String,
        val supply: String,
        val maxSupply: String,
        val marketCapUsd: String,
        val volumeUsd24Hr: String,
        val priceUsd: String,
        val changePercent24Hr: String,
        val vwap24Hr: String)


data class HistoryData(val priceUsd: String, val timestamp: Long)