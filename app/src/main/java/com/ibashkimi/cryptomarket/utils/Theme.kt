package com.ibashkimi.cryptomarket.utils

import android.content.Context
import android.util.TypedValue

fun Context.fetchColor(attr: Int): Int {
    val typedValue = TypedValue()

    val a = obtainStyledAttributes(typedValue.data, intArrayOf(attr))
    val color = a.getColor(0, 0)

    a.recycle()

    return color
}