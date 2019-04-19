package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataSource
import com.ibashkimi.cryptomarket.data.api.coincap.model.AssetItem
import com.ibashkimi.cryptomarket.data.api.coincap.model.toCoinList
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
        coinCapApi.getCoin(id).enqueue(object : Callback<AssetItem?> {
            override fun onFailure(call: Call<AssetItem?>, t: Throwable) {
                onResponse(ApiResponse.Failure())
            }

            override fun onResponse(call: Call<AssetItem?>, response: Response<AssetItem?>) {
                if (response.isSuccessful)
                    onResponse(ApiResponse.Success(response.body()!!.toCoinList()[0]))
                else
                    onResponse(ApiResponse.Failure())
            }
        })
    }
}