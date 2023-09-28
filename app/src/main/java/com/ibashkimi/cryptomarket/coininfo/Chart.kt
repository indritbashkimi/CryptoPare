package com.ibashkimi.cryptomarket.coininfo

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.utils.fetchColor

fun LineChart.configureChart() {
    description.isEnabled = false
    setTouchEnabled(true)
    isDragEnabled = false
    setScaleEnabled(false)
    setPinchZoom(false)
    setDrawGridBackground(false)

    xAxis.apply {
        isEnabled = false
    }

    axisLeft.apply {
        isEnabled = false
    }

    axisRight.isEnabled = false
    legend.isEnabled = false
}

fun LineDataSet.configureDataSet(chart: LineChart) {
    mode = LineDataSet.Mode.CUBIC_BEZIER
    cubicIntensity = 0.2f
    setDrawFilled(false)
    setDrawCircles(false)
    lineWidth = 1.8f
    circleRadius = 4f
    val accentColor = chart.context.fetchColor(R.attr.colorSecondary)
    color = accentColor
    fillColor = accentColor
    setDrawHorizontalHighlightIndicator(false)
}