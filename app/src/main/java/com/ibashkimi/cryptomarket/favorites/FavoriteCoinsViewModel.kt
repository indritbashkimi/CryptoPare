package com.ibashkimi.cryptomarket.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibashkimi.cryptomarket.data.UseCases
import com.ibashkimi.cryptomarket.model.Coin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FavoriteCoinsViewModel : ViewModel() {

    val coins = MutableLiveData<List<Coin>?>().apply { refresh() }

    fun refresh() {
        viewModelScope.launch {
            // TODO
            UseCases.favoriteCoins.collect {
                it.collect { res ->
                    coins.postValue(res)
                }
            }
        }
    }
}