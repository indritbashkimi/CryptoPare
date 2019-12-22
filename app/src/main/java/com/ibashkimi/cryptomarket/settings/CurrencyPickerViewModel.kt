package com.ibashkimi.cryptomarket.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibashkimi.cryptomarket.data.UseCases
import com.ibashkimi.cryptomarket.model.Currency
import kotlinx.coroutines.flow.map

class CurrencyPickerViewModel : ViewModel() {

    val currencies: LiveData<List<Currency>?> = UseCases.supportedCurrencies()
        .map { currencies -> currencies?.filter { it.type == "fiat" }?.sortedBy { it.symbol } }
        .asLiveData()

}