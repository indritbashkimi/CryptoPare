package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coinmarketcap.CoinMarketCapItem
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.CoinMarketCapService
import com.ibashkimi.cryptomarket.model.Coin
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

    fun getCoins(onSuccess: (data: List<Coin>) -> Unit, onFailure: () -> Unit) {
        coinMarketCapApi.listCoins(0, 100).enqueue(object : retrofit2.Callback<MutableList<CoinMarketCapItem>?> {
            override fun onFailure(call: retrofit2.Call<MutableList<CoinMarketCapItem>?>?, t: Throwable?) {
                onFailure()
            }

            override fun onResponse(call: retrofit2.Call<MutableList<CoinMarketCapItem>?>, response: retrofit2.Response<MutableList<CoinMarketCapItem>?>) {
                if (response.isSuccessful) {
                    val data: List<CoinMarketCapItem> = response.body() ?: emptyList()
                    onSuccess(data.toCoins())
                } else {
                    onFailure()
                }
            }
        })
    }

    fun getCoin(id: String, onSuccess: (data: Coin) -> Unit, onFailure: () -> Unit) {
        coinMarketCapApi.getCoin(id).enqueue(object : Callback<MutableList<CoinMarketCapItem>?> {
            override fun onFailure(call: Call<MutableList<CoinMarketCapItem>?>, t: Throwable?) {
                onFailure()
            }

            override fun onResponse(call: Call<MutableList<CoinMarketCapItem>?>, response: Response<MutableList<CoinMarketCapItem>?>) {
                if (response.isSuccessful)
                    onSuccess(response.body()!![0].toCoin())
                else
                    onFailure()
            }
        })
    }

    private fun CoinMarketCapItem.toCoin() = Coin(
            id = id,
            name = name,
            symbol = symbol,
            price = price_usd,
            percentChange1h = percent_change_1h,
            percentChange24h = percent_change_24h,
            percentChange7d = percent_change_7d
    )

    private fun List<CoinMarketCapItem>.toCoins(): List<Coin> {
        return this.map { it.toCoin() }
    }
}