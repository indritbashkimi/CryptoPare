package com.ibashkimi.cryptomarket.data.api.coinmarketcap

data class TickerQueryItem(val data: List<CoinMarketCapCoin>, val metadata: Metadata)

data class CoinTickerItem(val data: CoinMarketCapCoin, val metadata: Metadata)

data class CoinMarketCapCoin(val id: String,
                             val name: String,
                             val symbol: String,
                             val website_slug: String,
                             val rank: String,
                             val circulating_supply: String,
                             val total_supply: String,
                             val max_supply: String,
                             val quotes: Map<String, Quote>,
                             val last_updated: String)

data class Quote(val price: String,
                 val volume_24h: String,
                 val market_cap: String,
                 val percent_change_1h: String,
                 val percent_change_24h: String,
                 val percent_change_7d: String)

data class Metadata(val timestamp: String,
                    val num_cryptocurrencies: String,
                    val error: String?)

data class ListingItem(val data: List<ListingCoin>, val metadata: Metadata)

data class ListingCoin(val id: String,
                       val name: String,
                       val symbol: String,
                       val website_slug: String)
