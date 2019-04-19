package com.ibashkimi.cryptomarket.data.api.coincap;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * https://github.com/CoinCapDev/CoinCap.io
 */
public interface CoinCapService {
    String ENDPOINT = "http://coincap.io/";

    @GET("map")
    Call<List<MapItem>> map();

    // Returns all history on a coin
    @GET("/history/{coin}")
    Call<String> history(@Path("coin") String coin);

    //Returns 1 day of price history for a given coin
    @GET("/history/1day/{coin}")
    Call<String> history1Day(@Path("coin") String coin);

    /**
     * @param coin Coin
     * @return 7 days of price history for a given coin
     */
    @GET("/history/7day/{coin}")
    Call<String> history7Day(@Path("coin") String coin);

    /**
     * @param coin Coin
     * @return 7 days of price history for a given coin
     */
    @GET("/history/30day/{coin}")
    Call<String> history30Day(@Path("coin") String coin);

    /**
     * @param coin Coin
     * @return 90 days of price history for a given coin
     */
    @GET("/history/90day/{coin}")
    Call<String> history90Day(@Path("coin") String coin);

    /**
     * @param coin Coin
     * @return 180 days of price history for a given coin
     */
    @GET("/history/180day/{coin}")
    Call<String> history180Day(@Path("coin") String coin);

    /**
     * @param coin Coin
     * @return 365 days of price history for a given coin
     */
    @GET("/history/365day/{coin}")
    Call<String> history365Day(@Path("coin") String coin);

    /**
     *
     * @param period 1day, 30day, 90day, 180day, 365day
     * @param coin Coin
     * @return price history for a given coin
     */
    @GET("/history/{period}/{coin}")
    Call<String> history(@Path("period") String period, @Path("coin") String coin);

    /*private val coinCapService: CoinCapService by lazy {
        Retrofit.Builder()
                .baseUrl(CoinCapService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<CoinCapService>(CoinCapService::class.java)
    }*/
}
