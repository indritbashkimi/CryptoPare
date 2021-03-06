package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.ibashkimi.cryptomarket.App
import com.ibashkimi.cryptomarket.model.Coin

object PreferenceHelper {

    const val KEY_CURRENCY = "currency"
    const val KEY_CURRENCY_SYMBOL = "currency_symbol"
    const val KEY_CURRENCY_NAME = "currency_name"
    const val DEFAULT_CURRENCY = "united-states-dollar"

    const val KEY_NIGHT_MODE = "night_mode"
    const val DEFAULT_NIGHT_MODE = "auto"

    const val KEY_FAVORITE_COINS = "favorite_coins"

    val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(App.getInstance())

    var isFirstTime: Boolean
        get() = sharedPreferences.getBoolean("is_first_time", true)
        set(value) {
            sharedPreferences.edit { putBoolean("is_first_time", value) }
        }

    var currency: String
        get() = sharedPreferences.getString(KEY_CURRENCY, DEFAULT_CURRENCY)!!
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENCY, value).apply()
        }

    var currencySymbol: String?
        get() = sharedPreferences.getString(KEY_CURRENCY_SYMBOL, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENCY_SYMBOL, value).apply()
        }

    var currencyName: String
        get() = sharedPreferences.getString(KEY_CURRENCY_NAME, null) ?: ""
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENCY_NAME, value).apply()
        }

    val currencyFlow = preferenceFlow(KEY_CURRENCY) { currency }

    var nightMode: String
        get() = sharedPreferences.getString(KEY_NIGHT_MODE, DEFAULT_NIGHT_MODE)!!
        set(value) {
            sharedPreferences.edit().putString(KEY_NIGHT_MODE, value).apply()
        }

    var favoriteCoins: Set<String>
        get() = sharedPreferences.getStringSet(KEY_FAVORITE_COINS, null) ?: emptySet()
        set(value) {
            sharedPreferences.edit().putStringSet(KEY_FAVORITE_COINS, value).apply()
        }

    val favoriteCoinsFlow = preferenceFlow(KEY_FAVORITE_COINS) { favoriteCoins }

    fun initIfFirstTimeAnd(doAlso: () -> Unit = {}) {
        if (isFirstTime) {
            doAlso()
            isFirstTime = false
        }
    }

    fun addFavorite(coin: Coin) {
        val favorites: MutableList<String> = favoriteCoins.toMutableList()
        if (coin.id !in favorites) {
            favorites.add(coin.id)
            favoriteCoins = favorites.toSortedSet()
        }
    }

    fun removeFavorite(coin: Coin) {
        val favorites: MutableList<String> = favoriteCoins.toMutableList()
        favorites.remove(coin.id)
        favoriteCoins = favorites.toSortedSet()
    }

    fun isFavorite(coin: Coin): Boolean {
        return favoriteCoins.contains(coin.id)
    }
}
