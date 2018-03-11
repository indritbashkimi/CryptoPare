package com.ibashkimi.cryptomarket.data.api.coinmarketcap;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * CoinMarketCap API - https://coinmarketcap.com/api/
 */
public interface CoinMarketCapService {

    String ENDPOINT = "https://api.coinmarketcap.com/";
    int PER_PAGE_MAX = 100;
    int PER_PAGE_DEFAULT = 30;

    @GET("v1/ticker/")
    Call<List<CoinMarketCapItem>> getCoins(@Query("start") Integer start, @Query("limit") Integer limit);

    @GET("v1/ticker/")
    Call<List<CoinMarketCapItem>> getCoins(@Query("convert") String currency, @Query("start") Integer start, @Query("limit") Integer limit);

    @GET("v1/ticker/{id}/")
    Call<List<CoinMarketCapItem>> getCoin(@Path("id") String id);

    @GET("v1/ticker/{id}/")
    Call<List<CoinMarketCapItem>> getCoin(@Path("id") String id, @Query("convert") String currency);
}
