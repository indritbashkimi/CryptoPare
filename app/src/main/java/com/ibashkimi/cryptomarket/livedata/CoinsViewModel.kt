package com.ibashkimi.cryptomarket.livedata

import android.arch.lifecycle.ViewModel


class CoinsViewModel : ViewModel() {
    val coins = CoinsLiveData()
}