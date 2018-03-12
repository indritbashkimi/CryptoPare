package com.ibashkimi.cryptomarket.utils

import java.text.DecimalFormatSymbols

fun String.priceFormat(format: DecimalFormatSymbols = DecimalFormatSymbols()): String {
    val decimalSeparator = format.decimalSeparator
    val groupingSeparator = format.groupingSeparator
    val splits = split(".")

    var separatorsCount: Int = length / 3
    if (length % 3 == 0) {
        separatorsCount -= 1
    }
    val stringBuilder = StringBuilder()
    for (i in 0 until splits[0].length) {
        if (i != 0 && i % 3 == 0)
            stringBuilder.append(groupingSeparator)
        stringBuilder.append(splits[0][splits[0].length - 1 - i])
    }
    stringBuilder.reverse()
    if (splits.size > 1) {
        stringBuilder.append(decimalSeparator)
        stringBuilder.append(splits[1])
    }
    return stringBuilder.toString()
}
