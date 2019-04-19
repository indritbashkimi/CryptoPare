package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ibashkimi.cryptomarket.App
import com.ibashkimi.cryptomarket.model.Coin

object PreferenceHelper {

    const val KEY_CURRENCY = "currency"
    const val DEFAULT_CURRENCY = "USD"

    const val KEY_NIGHT_MODE = "night_mode"
    const val DEFAULT_NIGHT_MODE = "auto"

    const val KEY_FAVORITE_COINS = "favorite_coins"

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance())

    var currency: String
        get() = sharedPreferences.getString(KEY_CURRENCY, DEFAULT_CURRENCY)!!
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENCY, value).apply()
        }

    var nightMode: String
        get() = sharedPreferences.getString(KEY_NIGHT_MODE, DEFAULT_NIGHT_MODE)!!
        set(value) {
            sharedPreferences.edit().putString(KEY_NIGHT_MODE, value).apply()
        }

    var favoriteCoins: Set<String>?
        get() = sharedPreferences.getStringSet(KEY_FAVORITE_COINS, null)
        set(value) {
            sharedPreferences.edit().putStringSet(KEY_FAVORITE_COINS, value).apply()
        }

    fun addFavorite(coin: Coin) {
        val favorites: MutableList<String> = favoriteCoins?.toMutableList() ?: ArrayList(1)
        if (coin.id !in favorites) {
            favorites.add(coin.id)
            favoriteCoins = favorites.toSortedSet()
        }
    }

    fun removeFavorite(coin: Coin) {
        val favorites: MutableList<String> = favoriteCoins?.toMutableList() ?: ArrayList(1)
        favorites.remove(coin.id)
        favoriteCoins = favorites.toSortedSet()
    }

    fun isFavorite(coin: Coin): Boolean {
        return favoriteCoins?.contains(coin.id) ?: false
    }
}