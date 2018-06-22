package com.ibashkimi.cryptomarket.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.ibashkimi.cryptomarket.R;

public class CurrencySymbolResolver {

    @NonNull
    public static String resolve(@NonNull Context context, @NonNull String currency) {
        return context.getString(resolveRes(currency));
    }

    @StringRes
    private static int resolveRes(@NonNull String currency) {
        switch (currency) {
            case "AUD":
                return R.string.aud_symbol;
            case "BRL":
                return R.string.brl_symbol;
            case "BTC":
                return R.string.btc_symbol;
            case "CAD":
                return R.string.cad_symbol;
            case "CHF":
                return R.string.chf_symbol;
            case "CLP":
                return R.string.clp_symbol;
            case "CNY":
                return R.string.cny_symbol;
            case "CZK":
                return R.string.czk_symbol;
            case "DKK":
                return R.string.dkk_symbol;
            case "EUR":
                return R.string.eur_symbol;
            case "GBP":
                return R.string.gbp_symbol;
            case "HKD":
                return R.string.hkd_symbol;
            case "HUF":
                return R.string.huf_symbol;
            case "IDR":
                return R.string.idr_symbol;
            case "ILS":
                return R.string.ils_symbol;
            case "INR":
                return R.string.inr_symbol;
            case "JPY":
                return R.string.jpy_symbol;
            case "KRW":
                return R.string.krw_symbol;
            case "MXN":
                return R.string.mxn_symbol;
            case "MYR":
                return R.string.myr_symbol;
            case "NOK":
                return R.string.nok_symbol;
            case "NZD":
                return R.string.nzd_symbol;
            case "PHP":
                return R.string.php_symbol;
            case "PKR":
                return R.string.pkr_symbol;
            case "PLN":
                return R.string.pln_symbol;
            case "RUB":
                return R.string.rub_symbol;
            case "SEK":
                return R.string.sek_symbol;
            case "SGD":
                return R.string.sgd_symbol;
            case "THB":
                return R.string.thb_symbol;
            case "TRY":
                return R.string.try_symbol;
            case "TWD":
                return R.string.twd_symbol;
            case "USD":
                return R.string.usd_symbol;
            case "ZAR":
                return R.string.zar_symbol;
            default:
                throw new IllegalArgumentException("Unknown currency " + currency + ".");
        }
    }
}
