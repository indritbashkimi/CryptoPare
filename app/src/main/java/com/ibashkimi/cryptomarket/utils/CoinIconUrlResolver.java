package com.ibashkimi.cryptomarket.utils;


import com.ibashkimi.cryptomarket.model.Coin;

public class CoinIconUrlResolver {
    private static final String BASE_URL = "https://raw.githubusercontent.com/cjdowner/cryptocurrency-icons/master/128/icon/";

    public static String resolve(Coin coin) {
        return BASE_URL + coin.getSymbol().toLowerCase() + ".png";
    }

    public static String resolve(String coinSymbol) {
        return BASE_URL + coinSymbol.toLowerCase() + ".png";
    }
}
