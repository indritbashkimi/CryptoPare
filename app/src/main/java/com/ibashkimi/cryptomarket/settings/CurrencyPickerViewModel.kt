package com.ibashkimi.cryptomarket.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibashkimi.cryptomarket.data.api.coincap.CoinCapApiImplementor
import com.ibashkimi.cryptomarket.data.api.coincap.model.RateItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyPickerViewModel : ViewModel() {

    val currencies = MutableLiveData<List<RateItem>?>()

    init {
        refresh()
    }

    fun setCurrency(rateId: String) {
        PreferenceHelper.currency = rateId
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            currencies.postValue(CoinCapApiImplementor().getRates()
                    ?.data?.filter { it.type == "fiat" }?.sortedBy { it.symbol })
        }
    }
}