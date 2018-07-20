package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapService
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.TickerQueryItem
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.CoinMarketCapService
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.CoinTickerItem
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.toCoin
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.toCoins
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object DataManager {

    private val coinMarketCapApi: CoinMarketCapService by lazy {
        Retrofit.Builder()
                .baseUrl(CoinMarketCapService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<CoinMarketCapService>(CoinMarketCapService::class.java)
    }

    private val coinCapService: CoinCapService by lazy {
        Retrofit.Builder()
                .baseUrl(CoinCapService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<CoinCapService>(CoinCapService::class.java)
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
        val call: Call<TickerQueryItem> = coinMarketCapApi.getCoins(0, 0, "array")
        call.enqueue(object : Callback<TickerQueryItem?> {
            override fun onFailure(call: Call<TickerQueryItem?>, t: Throwable) {
                onResponse(ApiResponse.Failure(t.toString()))
            }

            override fun onResponse(call: Call<TickerQueryItem?>, response: Response<TickerQueryItem?>) {
                onResponse(ApiResponse.Success(response.body()?.toCoins() ?: emptyList()))
            }
        })
    }

    fun getCoins(start: Int, limit: Int, currency: String, onSuccess: (data: List<Coin>) -> Unit, onFailure: () -> Unit) {
        val call = coinMarketCapApi.getCoins(currency, start, limit, "array")
        call.enqueue(object : retrofit2.Callback<TickerQueryItem?> {
            override fun onFailure(call: retrofit2.Call<TickerQueryItem?>?, t: Throwable?) {
                onFailure()
            }

            override fun onResponse(call: retrofit2.Call<TickerQueryItem?>, response: retrofit2.Response<TickerQueryItem?>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()?.toCoins(currency) ?: emptyList())
                } else {
                    onFailure()
                }
            }
        })
    }

    fun getCoin(id: String, currency: String, onSuccess: (data: Coin) -> Unit, onFailure: () -> Unit) {
        coinMarketCapApi.getCoin(id, currency).enqueue(object : Callback<CoinTickerItem?> {
            override fun onFailure(call: Call<CoinTickerItem?>, t: Throwable?) {
                onFailure()
            }

            override fun onResponse(call: Call<CoinTickerItem?>, response: Response<CoinTickerItem?>) {
                if (response.isSuccessful)
                    onSuccess(response.body()!!.data.toCoin(currency))
                else
                    onFailure()
            }
        })
    }

    fun coin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit) {
        coinMarketCapApi.getCoin(id, currency).enqueue(object : Callback<CoinTickerItem?> {
            override fun onFailure(call: Call<CoinTickerItem?>, t: Throwable) {
                onResponse(ApiResponse.Failure(t.toString()))
            }

            override fun onResponse(call: Call<CoinTickerItem?>, response: Response<CoinTickerItem?>) {
                if (response.isSuccessful)
                    onResponse(ApiResponse.Success(response.body()!!.data.toCoin(currency)))
                else
                    onResponse(ApiResponse.Failure(""))
            }
        })
    }

    fun history(coinId: String, period: HistoryPeriod, onResponse: (ApiResponse<String>) -> Unit) {
        Retrofit.Builder()
                .baseUrl(CoinCapService.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create<CoinCapService>(CoinCapService::class.java).apply {
                    when (period) {
                        HistoryPeriod.DAY -> history1Day(coinId)
                        HistoryPeriod.MONTH -> history30Day(coinId)
                        HistoryPeriod.MONTH3 -> history90Day(coinId)
                        HistoryPeriod.MONTH6 -> history180Day(coinId)
                        HistoryPeriod.YEAR -> history365Day(coinId)
                        HistoryPeriod.ALL -> history(coinId)
                    }.enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            onResponse(ApiResponse.Failure(t.toString()))
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            onResponse(ApiResponse.Success(response.body()!!))
                        }
                    })
                }
    }



    enum class HistoryPeriod {
        ALL, DAY, MONTH, MONTH3, MONTH6, YEAR
    }
}