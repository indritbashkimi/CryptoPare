package com.ibashkimi.cryptomarket.model


data class Coin(
        val id: String,
        val name: String,
        val symbol: String,
        val price: String,
        val percentChange1h: String? = null,
        val percentChange24h: String? = null,
        val percentChange7d: String? = null
)