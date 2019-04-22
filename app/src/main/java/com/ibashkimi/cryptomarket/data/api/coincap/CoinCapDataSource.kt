package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataSource
import com.ibashkimi.cryptomarket.data.HistoryPeriod
import com.ibashkimi.cryptomarket.data.api.coincap.model.*
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CoinCapDataSource : DataSource {

    private val coinCapApi: CoinCapApi by lazy {
        Retrofit.Builder()
                .baseUrl(CoinCapApi.ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create<CoinCapApi>(CoinCapApi::class.java)
    }

    override fun getCoins(start: Int, limit: Int, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        coinCapApi.getCoins(start, limit).enqueue(object : Callback<AssetItem?> {
            override fun onFailure(call: Call<AssetItem?>, t: Throwable) {
                onResponse(ApiResponse.Failure(t))
            }

            override fun onResponse(call: Call<AssetItem?>, response: Response<AssetItem?>) {
                if (response.isSuccessful) {
                    val data = response.body()?.toCoinList()
                    val res: ApiResponse<List<Coin>> = if (data != null) {
                        ApiResponse.Success(data)
                    } else {
                        ApiResponse.Failure()
                    }
                    onResponse(res)
                } else {
                    onResponse(ApiResponse.Failure())
                }
            }
        })
    }

    override fun getCoins(ids: List<String>, onResponse: (ApiResponse<List<Coin>>) -> Unit) {
        coinCapApi.getCoins(ids).enqueue(object : Callback<AssetItem?> {
            override fun onFailure(call: Call<AssetItem?>, t: Throwable) {
                onResponse(ApiResponse.Failure(t))
            }

            override fun onResponse(call: Call<AssetItem?>, response: Response<AssetItem?>) {
                if (response.isSuccessful) {
                    val data = response.body()?.toCoinList()
                    val res: ApiResponse<List<Coin>> = if (data != null) {
                        ApiResponse.Success(data)
                    } else {
                        ApiResponse.Failure()
                    }
                    onResponse(res)
                } else {
                    onResponse(ApiResponse.Failure())
                }
            }
        })
    }

    override fun getCoin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit) {
        coinCapApi.getCoin(id).enqueue(object : Callback<CoinItem?> {
            override fun onFailure(call: Call<CoinItem?>, t: Throwable) {
                onResponse(ApiResponse.Failure())
            }

            override fun onResponse(call: Call<CoinItem?>, response: Response<CoinItem?>) {
                if (response.isSuccessful)
                    onResponse(ApiResponse.Success(response.body()!!.toCoin()))
                else
                    onResponse(ApiResponse.Failure())
            }
        })
    }

    override fun getHistory(id: String, interval: String, onResponse: (ApiResponse<List<ChartPoint>>) -> Unit) {
        coinCapApi.getCoinHistory(id, interval).enqueue(object: Callback<HistoryItem?> {
            override fun onFailure(call: Call<HistoryItem?>, t: Throwable) {
                onResponse(ApiResponse.Failure())
            }

            override fun onResponse(call: Call<HistoryItem?>, response: Response<HistoryItem?>) {
                if (response.isSuccessful) {
                    onResponse(ApiResponse.Success(response.body()!!.toChartPointList()))
                } else {
                    onResponse(ApiResponse.Failure())
                }
            }
        })
    }
}