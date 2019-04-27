package com.ibashkimi.cryptomarket.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper


class FavoriteCoinsViewModel : ViewModel() {

    var coins: MutableLiveData<List<Coin>?> = MutableLiveData()

    init {
        refresh()
    }

    fun refresh() {
        val favorites = PreferenceHelper.favoriteCoins.toList()
        if (favorites.isEmpty()) {
            coins.value = emptyList()
        } else {
            DataManager.getCoins(favorites, "USD") {
                when (it) {
                    is ApiResponse.Success -> coins.value = it.result
                    is ApiResponse.Failure -> coins.value = null
                }
            }
        }
    }
}