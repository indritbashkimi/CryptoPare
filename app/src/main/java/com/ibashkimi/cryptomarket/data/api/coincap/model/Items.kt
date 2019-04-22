package com.ibashkimi.cryptomarket.data.api.coincap.model


data class AssetItem(val data: List<Asset>, val timestamp: Long)

data class CoinItem(val data: Asset, val timestamp: Long)

data class HistoryItem(val data: List<HistoryPoint>, val timestamp: Long)

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

data class HistoryPoint(val priceUsd: String, val time: Long)