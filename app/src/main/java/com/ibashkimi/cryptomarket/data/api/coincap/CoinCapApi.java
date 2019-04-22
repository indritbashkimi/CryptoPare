package com.ibashkimi.cryptomarket.data.api.coincap;

import com.ibashkimi.cryptomarket.data.api.coincap.model.AssetItem;
import com.ibashkimi.cryptomarket.data.api.coincap.model.CoinItem;
import com.ibashkimi.cryptomarket.data.api.coincap.model.HistoryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * https://docs.coincap.io/
 */
public interface CoinCapApi {
    String ENDPOINT = "https://api.coincap.io/";

    @GET("v2/assets/")
    Call<AssetItem> getCoins(@Query("offset") Integer start, @Query("limit") Integer limit);

    @GET("v2/assets/")
    Call<AssetItem> getCoins(@Query("ids") List<String> ids);

    @GET("v2/assets/{id}/")
    Call<CoinItem> getCoin(@Path("id") String id);

    @GET("v2/assets/{id}/history")
    Call<HistoryItem> getCoinHistory(@Path("id") String id, @Query("interval") String interval);

    @GET("v2/assets/")
    Call<AssetItem> search(@Query("search") String search, @Query("offset") Integer start, @Query("limit") Integer limit);
}

