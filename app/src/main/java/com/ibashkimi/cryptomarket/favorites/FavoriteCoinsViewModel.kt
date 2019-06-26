package com.ibashkimi.cryptomarket.favorites

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper


class FavoriteCoinsViewModel : ViewModel() {

    val coins = MediatorLiveData<List<Coin>?>().apply {
        addSource(PreferenceHelper.favoriteCoinsLiveData) {
            refresh(it)
        }
    }

    fun refresh() {
        refresh(PreferenceHelper.favoriteCoins)
    }

    private fun refresh(favorites: Set<String>) {
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