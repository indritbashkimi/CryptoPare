package com.ibashkimi.cryptomarket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.data.HistoryPeriod
import com.ibashkimi.cryptomarket.utils.toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ChartFragment : Fragment() {
    private lateinit var chart: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chart = view.findViewById(R.id.chart1) as LineChart
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        //chart.setBackgroundColor(Color.rgb(104, 241, 175))

        DataManager.history(arguments!!.getString("coin_symbol")!!, HistoryPeriod.MONTH3) {
            when (it) {
                is ApiResponse.Success -> {
                    //toast("history success")
                    //Log.d("Puttana", "price: ${it.result.price}")
                    val priceArray = ArrayList<Pair<Long, Double>>()
                    try {
                        val jsonObject = JSONObject(it.result)
                        val priceJsonArray = jsonObject.getJSONArray("price")
                        for (i in 0 until priceJsonArray.length()) {
                            val pointJsonArray = priceJsonArray.get(i) as JSONArray
                            priceArray.add(Pair(pointJsonArray.getLong(0), pointJsonArray.getDouble(1)))
                        }
                        Log.d("Puttana ", "$priceArray")

                        chart.apply {
                            description.isEnabled = false
                            setTouchEnabled(true)
                            isDragEnabled = false
                            setScaleEnabled(false)
                            setPinchZoom(false)
                            setDrawGridBackground(false)
                            //chart.setMaxHighlightDistance(300)

                            xAxis.apply {
                                isEnabled = false
                            }

                            axisLeft.apply {
                                isEnabled = false
                            }

                            axisRight.isEnabled = false

                            // add data
                            setData(priceArray)

                            legend.isEnabled = false

                            animateXY(1000, 1000)

                            // don't forget to refresh the drawing
                            invalidate()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                is ApiResponse.Failure -> {
                    toast("history failure ${it.error}")
                }
            }
        }
    }

    private fun setData(values: ArrayList<Pair<Long, Double>>) {

        val yVals = ArrayList<Entry>()

        for (value in values) {
            yVals.add(Entry(value.first.toFloat(), value.second.toFloat()))
        }

        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = yVals
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(yVals, "DataSet 1")

            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(false)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            //set1.setCircleColor(Color.WHITE)
            //set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
            //set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter = IFillFormatter { _, _ -> -10f }

            // create a data object with the datasets
            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            chart.data = data
        }
    }

    companion object {
        fun newInstance(coinSymbol: String): ChartFragment {
            val fragment = ChartFragment()
            fragment.arguments = bundleOf(Pair("coin_symbol", coinSymbol))
            return fragment
        }
    }
}