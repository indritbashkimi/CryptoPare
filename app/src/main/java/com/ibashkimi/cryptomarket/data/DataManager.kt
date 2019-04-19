package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapApi
import com.ibashkimi.cryptomarket.data.api.coincap.model.AssetItem
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCoinList
import com.ibashkimi.cryptomarket.data.api.coinmarketcap.*
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.SearchItem
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object DataManager {

    private val coinCapApi: CoinCapApi by lazy {
        Retrofit.Builder()
                .baseUrl(CoinCapApi.ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create<CoinCapApi>(CoinCapApi::class.java)
    }

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

    fun loadSupportedCoins(onResponse: (ApiResponse<List<SearchItem>>) -> Unit) {
        coinMarketCapApi.listing().enqueue(object : Callback<ListingItem> {
            override fun onFailure(call: Call<ListingItem>, t: Throwable?) {
                onResponse(ApiResponse.Failure(t.toString()))
            }

            override fun onResponse(call: Call<ListingItem>, response: Response<ListingItem>) {
                onResponse(ApiResponse.Success(response.body()?.toSearchItems() ?: emptyList()))
            }
        })
    }

    fun getCoins(start: Int, limit: Int, currency: String, onSuccess: (data: List<Coin>) -> Unit, onFailure: () -> Unit) {
        coinCapApi.getCoins(start, limit).enqueue(object : Callback<AssetItem?> {
            override fun onFailure(call: Call<AssetItem?>, t: Throwable) {
                onFailure()
            }

            override fun onResponse(call: Call<AssetItem?>, response: Response<AssetItem?>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()?.toCoinList() ?: emptyList())
                } else {
                    onFailure()
                }
            }
        })
    }

    fun getCoin(id: String, currency: String, onSuccess: (data: Coin) -> Unit, onFailure: () -> Unit) {
        android.util.Log.d("DataManager", "requesting id $id")
        coinCapApi.getCoin(id).enqueue(object : Callback<AssetItem?> {
            override fun onFailure(call: Call<AssetItem?>, t: Throwable) {
                android.util.Log.d("DataManager", "Fuck failure")
                onFailure()
            }

            override fun onResponse(call: Call<AssetItem?>, response: Response<AssetItem?>) {
                android.util.Log.d("DataManager", "Gentlemen we've got a response. success? ${response.isSuccessful}")
                if (response.isSuccessful)
                    onSuccess(response.body()!!.toCoinList()[0])
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
        onResponse(ApiResponse.Failure(""))
    }


    enum class HistoryPeriod {
        ALL, DAY, MONTH, MONTH3, MONTH6, YEAR
    }
}