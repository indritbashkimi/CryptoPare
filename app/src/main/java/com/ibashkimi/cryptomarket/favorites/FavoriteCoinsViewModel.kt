package com.ibashkimi.cryptomarket.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibashkimi.cryptomarket.data.UseCases
import com.ibashkimi.cryptomarket.model.Coin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FavoriteCoinsViewModel : ViewModel() {

    val coins = MutableLiveData<List<Coin>?>()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            UseCases.favoriteCoins.collect {
                coins.postValue(it)
            }
        }
    }
}