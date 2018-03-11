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

    fun getCoins(start: Int, limit: Int, currency: String, onSuccess: (data: List<Coin>) -> Unit, onFailure: () -> Unit) {
        coinMarketCapApi.getCoins(currency, start, limit).enqueue(object : retrofit2.Callback<MutableList<CoinMarketCapItem>?> {
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


    private fun CoinMarketCapItem.toCoin(currency: String = PreferenceHelper.DEFAULT_CURRENCY): Coin {
        var price: String? = price_usd
        when (currency) {
            "AUD" -> {
                price = price_aud
            }
            "BRL" -> {
                price = price_brl
            }
            "BTC" -> {
                price = price_btc
            }
            "CAD" -> {
                price = price_cad
            }
            "CHF" -> {
                price = price_chf
            }
            "CLP" -> {
                price = price_clp
            }
            "CNY" -> {
                price = price_cny
            }
            "CZK" -> {
                price = price_czk
            }
            "DKK" -> {
                price = price_dkk
            }
            "EUR" -> {
                price = price_eur
            }
            "GBP" -> {
                price = price_gbp
            }
            "HKD" -> {
                price = price_hkd
            }
            "HUF" -> {
                price = price_huf
            }
            "IDR" -> {
                price = price_idr
            }
            "ILS" -> {
                price = price_ils
            }
            "INR" -> {
                price = price_inr
            }
            "JPY" -> {
                price = price_jpy
            }
            "KRW" -> {
                price = price_krw
            }
            "MXN" -> {
                price = price_mxn
            }
            "MYR" -> {
                price = price_myr
            }
            "NOK" -> {
                price = price_nok
            }
            "NZD" -> {
                price = price_nzd
            }
            "PHP" -> {
                price = price_php
            }
            "PKR" -> {
                price = price_pkr
            }
            "PLN" -> {
                price = price_pln
            }
            "RUB" -> {
                price = price_rub
            }
            "SEK" -> {
                price = price_sek
            }
            "SGD" -> {
                price = price_sgd
            }
            "THB" -> {
                price = price_thb
            }
            "TRY" -> {
                price = price_try
            }
            "TWD" -> {
                price = price_twd
            }
            "USD" -> {
                price = price_usd
            }
            "ZAR" -> {
                price = price_zar
            }
        }

        return Coin(
                id = id,
                name = name,
                symbol = symbol,
                price = price,
                rank = rank,
                percentChange1h = percent_change_1h,
                percentChange24h = percent_change_24h,
                percentChange7d = percent_change_7d,
                lastUpdated = last_updated,
                currency = currency
        )
    }

    private fun List<CoinMarketCapItem>.toCoins(currency: String = PreferenceHelper.DEFAULT_CURRENCY): List<Coin> {
        return this.map { it.toCoin(currency) }
    }
}