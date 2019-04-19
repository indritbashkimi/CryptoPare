package com.ibashkimi.cryptomarket.data

import com.ibashkimi.cryptomarket.model.Coin

interface DataSource {

    fun getCoins(start: Int, limit: Int, currency: String, onResponse: (ApiResponse<List<Coin>>) -> Unit)

    fun getCoins(ids: List<String>, onResponse: (ApiResponse<List<Coin>>) -> Unit)

    fun getCoin(id: String, currency: String, onResponse: (ApiResponse<Coin>) -> Unit)

    fun getHistory(id: String, period: HistoryPeriod, onResponse: (ApiResponse<String>) -> Unit) {
        onResponse(ApiResponse.Failure(""))
    }
}

enum class HistoryPeriod {
    ALL, DAY, MONTH, MONTH3, MONTH6, YEAR
}