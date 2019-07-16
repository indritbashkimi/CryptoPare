package com.ibashkimi.cryptomarket.favorites

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            viewModelScope.launch(Dispatchers.IO) {
                coins.postValue(DataManager.getCoins(favorites, "USD"))
            }
        }
    }
}