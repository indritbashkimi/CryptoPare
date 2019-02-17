package com.ibashkimi.cryptomarket.data.api.coinmarketcap;

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

    @GET("v2/ticker/")
    Call<TickerQueryItem> getCoins(@Query("start") Integer start, @Query("limit") Integer limit, @Query("structure") String structure);

    @GET("v2/ticker/")
    Call<TickerQueryItem> getCoins(@Query("convert") String currency, @Query("start") Integer start, @Query("limit") Integer limit, @Query("structure") String structure);

    @GET("v2/ticker/{id}/")
    Call<CoinTickerItem> getCoin(@Path("id") String id);

    @GET("v2/ticker/{id}/")
    Call<CoinTickerItem> getCoin(@Path("id") String id, @Query("convert") String currency);

    @GET("v2/listing/")
    Call<ListingItem> listing();
}
