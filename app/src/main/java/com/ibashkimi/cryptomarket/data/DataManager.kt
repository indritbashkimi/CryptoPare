package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coinmarketcap.CoinMarketCapItem
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.CoinMarketCapService
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object DataManager {

    private val coinMarketCapApi: CoinMarketCapService by lazy {
        Retrofit.Builder()
                .baseUrl(CoinMarketCapService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<CoinMarketCapService>(CoinMarketCapService::class.java)
    }

    fun favoriteCoins(onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        val favoriteCoins = ArrayList<Coin>()
        var failure = false

        PreferenceHelper.favoriteCoins?.let { favorites ->
            for (coinId in favorites) {
                coin(coinId, PreferenceHelper.currency, onResponse = {
                    synchronized(this) {
                        if (failure)
                            return@synchronized
                        when (it) {
                            is ApiResponse.Success -> {
                                favoriteCoins.add(it.result)
                                if (favoriteCoins.size == favorites.size)
                                    onResponse(ApiResponse.Success(favoriteCoins))
                            }
                            is ApiResponse.Failure -> {
                                failure = true
                                onResponse(ApiResponse.Failure(""))
                            }
                        }
                    }

                })
            }
        }
    }

    fun loadSupportedCoins(onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        val call: Call<List<CoinMarketCapItem>> = coinMarketCapApi.getCoins(0, 0)
        call.enqueue(object : Callback<List<CoinMarketCapItem>?> {
            override fun onFailure(call: Call<List<CoinMarketCapItem>?>, t: Throwable) {
                onResponse(ApiResponse.Failure(t.toString()))
            }

            override fun onResponse(call: Call<List<CoinMarketCapItem>?>, response: Response<List<CoinMarketCapItem>?>) {
                onResponse(ApiResponse.Success(response.body()?.toCoins() ?: emptyList()))
            }
        })
    }

    fun getCoins(start: Int, limit: Int, currency: String, onSuccess: (data: List<Coin>) -> Unit, onFailure: () -> Unit) {
        val call = coinMarketCapApi.getCoins(currency, start, limit)
        call.enqueue(object : retrofit2.Callback<MutableList<CoinMarketCapItem>?> {
            override fun onFailure(call: retrofit2.Call<MutableList<CoinMarketCapItem>?>?, t: Throwable?) {
                onFailure()
            }

            override fun onResponse(call: retrofit2.Call<MutableList<CoinMarketCapItem>?>, response: retrofit2.Response<MutableList<CoinMarketCapItem>?>) {
                if (response.isSuccessful) {
                    val data: List<CoinMarketCapItem> = response.body() ?: emptyList()
                    onSuccess(data.toCoins(currency))
                } else {
                    onFailure()
                }
            }
        })
    }

    fun getCoin(id: String, currency: String, onSuccess: (data: Coin) -> Unit, onFailure: () -> Unit) {
        coinMarketCapApi.getCoin(id, currency).enqueue(object : Callback<MutableList<CoinMarketCapItem>?> {
            override fun onFailure(call: Call<MutableList<CoinMarketCapItem>?>, t: Throwable?) {
                onFailure()
            }

            override fun onResponse(call: Call<MutableList<CoinMarketCapItem>?>, response: Response<MutableList<CoinMarketCapItem>?>) {
                if (response.isSuccessful)
                    onSuccess(response.body()!![0].toCoin(currency))
                else
                    onFailure()
            }
        })
    }

    fun coin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit) {
        coinMarketCapApi.getCoin(id, currency).enqueue(object : Callback<MutableList<CoinMarketCapItem>?> {
            override fun onFailure(call: Call<MutableList<CoinMarketCapItem>?>, t: Throwable) {
                onResponse(ApiResponse.Failure(t.toString()))
            }

            override fun onResponse(call: Call<MutableList<CoinMarketCapItem>?>, response: Response<MutableList<CoinMarketCapItem>?>) {
                if (response.isSuccessful)
                    onResponse(ApiResponse.Success(response.body()!![0].toCoin(currency)))
                else
                    onResponse(ApiResponse.Failure(""))
            }
        })
    }

    private fun CoinMarketCapItem.toCoin(currency: String = PreferenceHelper.DEFAULT_CURRENCY): Coin {
        var price: String? = price_usd
        var market_cap: String? = market_cap_usd
        var volume_24h: String? = volume_24h_usd

        when (currency) {
            "AUD" -> {
                price = price_aud
                market_cap = market_cap_aud
                volume_24h = volume_24h_aud
            }
            "BRL" -> {
                price = price_brl
                market_cap = market_cap_brl
                volume_24h = volume_24h_brl
            }
            "BTC" -> {
                price = price_btc
                market_cap = market_cap_btc
                volume_24h = volume_24h_btc
            }
            "CAD" -> {
                price = price_cad
                market_cap = market_cap_cad
                volume_24h = volume_24h_cad
            }
            "CHF" -> {
                price = price_chf
                market_cap = market_cap_chf
                volume_24h = volume_24h_chf
            }
            "CLP" -> {
                price = price_clp
                market_cap = market_cap_clp
                volume_24h = volume_24h_clp
            }
            "CNY" -> {
                price = price_cny
                market_cap = market_cap_cny
                volume_24h = volume_24h_cny
            }
            "CZK" -> {
                price = price_czk
                market_cap = market_cap_czk
                volume_24h = volume_24h_czk
            }
            "DKK" -> {
                price = price_dkk
                market_cap = market_cap_dkk
                volume_24h = volume_24h_dkk
            }
            "EUR" -> {
                price = price_eur
                market_cap = market_cap_eur
                volume_24h = volume_24h_eur
            }
            "GBP" -> {
                price = price_gbp
                market_cap = market_cap_gbp
                volume_24h = volume_24h_gbp
            }
            "HKD" -> {
                price = price_hkd
                market_cap = market_cap_hkd
                volume_24h = volume_24h_hkd
            }
            "HUF" -> {
                price = price_huf
                market_cap = market_cap_huf
                volume_24h = volume_24h_huf
            }
            "IDR" -> {
                price = price_idr
                market_cap = market_cap_idr
                volume_24h = volume_24h_idr
            }
            "ILS" -> {
                price = price_ils
                market_cap = market_cap_ils
                volume_24h = volume_24h_ils
            }
            "INR" -> {
                price = price_inr
                market_cap = market_cap_inr
                volume_24h = volume_24h_inr
            }
            "JPY" -> {
                price = price_jpy
                market_cap = market_cap_jpy
                volume_24h = volume_24h_jpy
            }
            "KRW" -> {
                price = price_krw
                market_cap = market_cap_krw
                volume_24h = volume_24h_krw
            }
            "MXN" -> {
                price = price_mxn
                market_cap = market_cap_mxn
                volume_24h = volume_24h_mxn
            }
            "MYR" -> {
                price = price_myr
                market_cap = market_cap_myr
                volume_24h = volume_24h_myr
            }
            "NOK" -> {
                price = price_nok
                market_cap = market_cap_nok
                volume_24h = volume_24h_nok
            }
            "NZD" -> {
                price = price_nzd
                market_cap = market_cap_nzd
                volume_24h = volume_24h_nzd
            }
            "PHP" -> {
                price = price_php
                market_cap = market_cap_php
                volume_24h = volume_24h_php
            }
            "PKR" -> {
                price = price_pkr
                market_cap = market_cap_pkr
                volume_24h = volume_24h_pkr
            }
            "PLN" -> {
                price = price_pln
                market_cap = market_cap_pln
                volume_24h = volume_24h_pln
            }
            "RUB" -> {
                price = price_rub
                market_cap = market_cap_rub
                volume_24h = volume_24h_rub
            }
            "SEK" -> {
                price = price_sek
                market_cap = market_cap_sek
                volume_24h = volume_24h_sek
            }
            "SGD" -> {
                price = price_sgd
                market_cap = market_cap_sgd
                volume_24h = volume_24h_sgd
            }
            "THB" -> {
                price = price_thb
                market_cap = market_cap_thb
                volume_24h = volume_24h_thb
            }
            "TRY" -> {
                price = price_try
                market_cap = market_cap_try
                volume_24h = volume_24h_try
            }
            "TWD" -> {
                price = price_twd
                market_cap = market_cap_twd
                volume_24h = volume_24h_twd
            }
        /*"USD" -> {
            price = price_usd
            market_cap = market_cap_usd
            volume_24h = volume_24h_usd
        }*/
            "ZAR" -> {
                price = price_zar
                market_cap = market_cap_zar
                volume_24h = volume_24h_zar
            }
        }

        return Coin(
                id = id,
                name = name,
                symbol = symbol,
                price = price,
                priceBtc = price_btc,
                rank = rank,
                percentChange1h = percent_change_1h,
                percentChange24h = percent_change_24h,
                percentChange7d = percent_change_7d,
                volume24h = volume_24h,
                marketCap = market_cap,
                availableSupply = available_supply,
                totalSupply = total_supply,
                maxSupply = max_supply,
                lastUpdated = last_updated,
                currency = currency
        )
    }

    private fun List<CoinMarketCapItem>.toCoins(currency: String = PreferenceHelper.DEFAULT_CURRENCY): List<Coin> {
        return this.map { it.toCoin(currency) }
    }
}