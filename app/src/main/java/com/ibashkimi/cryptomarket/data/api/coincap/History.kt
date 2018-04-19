package com.ibashkimi.cryptomarket.data.api.coincap

class History(val market_cap: Array<Point> = emptyArray(), val price: Array<Point> = emptyArray(), val volume: Array<Point> = emptyArray())

data class Point(val unixTimeMillis: String, val marketCap: String)