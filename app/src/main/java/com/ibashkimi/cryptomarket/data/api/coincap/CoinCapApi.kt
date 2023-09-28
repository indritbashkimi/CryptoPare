package com.ibashkimi.cryptomarket.data.api.coincap

import com.ibashkimi.cryptomarket.data.api.coincap.model.*

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * https://docs.coincap.io/
 */
interface CoinCapApi {

    @GET("v2/assets/")
    suspend fun getCoins(
        @Query("offset") start: Int?,
        @Query("limit") limit: Int?
    ): Response<AssetItem>

    @GET("v2/assets/")
    suspend fun getCoins(@Query("ids") ids: String): Response<AssetItem>

    @GET("v2/assets/{id}/")
    suspend fun getCoin(@Path("id") id: String): Response<CoinItem>

    @GET("v2/assets/{id}/history")
    suspend fun getCoinHistory(
        @Path("id") id: String,
        @Query("interval") interval: String
    ): Response<HistoryItem>

    @GET("v2/assets/")
    suspend fun search(
        @Query("search") search: String,
        @Query("offset") start: Int?,
        @Query("limit") limit: Int?
    ): Response<AssetItem>

    @GET("v2/rates")
    suspend fun getRates(): Response<RatesResult>

    @GET("v2/rates/{id}")
    suspend fun getRate(@Path("id") id: String): Response<RateResult>

    companion object {
        const val ENDPOINT = "https://api.coincap.io/"
    }
}

