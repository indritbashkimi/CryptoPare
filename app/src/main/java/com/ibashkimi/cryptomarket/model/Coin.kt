package com.ibashkimi.cryptomarket.model


data class Coin(
        val id: String,
        val rank: String,
        val symbol: String,
        val name: String,
        val price: String,

        val percentChange1h: String? = null,
        val percentChange24h: String? = null,
        val percentChange7d: String? = null,
        val marketCap: String? = null,
        /**
         * Quantity available for trading
         */
        val availableSupply: String? = null,
        val volume24h: String? = null,
        /**
         * Total quantity of a coin
         */
        val totalSupply: String? = null,
        val maxSupply: String? = null,
        val lastUpdated: String? = "-1",
        val currency: String = "USD",
        val averagePrice24h: String? = null
)