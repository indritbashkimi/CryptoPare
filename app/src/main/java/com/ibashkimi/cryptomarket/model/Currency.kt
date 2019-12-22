package com.ibashkimi.cryptomarket.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Currency(
    val id: String,
    val symbol: String,
    val currencySymbol: String?,
    val rateUsd: String,
    val type: String// type of currency - fiat or crypto
)