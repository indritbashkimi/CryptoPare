package com.ibashkimi.cryptomarket.model

data class HistoryKey(val chartInterval: ChartInterval, val key: String)

fun historyKeysOf(vararg pairs: Pair<ChartInterval, String>): Set<HistoryKey> {
    return pairs.map { HistoryKey(it.first, it.second) }.toSet()
}