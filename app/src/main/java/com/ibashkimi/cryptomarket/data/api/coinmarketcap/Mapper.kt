package com.ibashkimi.cryptomarket.data.api.coinmarketcap

import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper

fun CoinMarketCapCoin.toCoin(currency: String = PreferenceHelper.DEFAULT_CURRENCY): Coin {

    val quote = this.quotes[currency]!!

    return Coin(
            id = id,
            name = name,
            symbol = symbol,
            price = quote.price,
            rank = rank,
            percentChange1h = quote.percent_change_1h,
            percentChange24h = quote.percent_change_24h,
            percentChange7d = quote.percent_change_7d,
            volume24h = quote.volume_24h,
            marketCap = quote.market_cap,
            availableSupply = total_supply,
            totalSupply = total_supply,
            maxSupply = max_supply,
            lastUpdated = last_updated,
            currency = currency
    )
}
