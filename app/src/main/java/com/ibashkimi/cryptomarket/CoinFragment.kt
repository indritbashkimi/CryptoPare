package com.ibashkimi.cryptomarket

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibashkimi.cryptomarket.livedata.CoinViewModel
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.CurrencySymbolResolver
import com.ibashkimi.cryptomarket.utils.toast
import java.text.DecimalFormat
import android.R.attr
import android.R.attr.colorAccent
import android.R.attr.data
import android.content.res.TypedArray
import android.util.TypedValue
import com.google.android.material.tabs.TabLayout


class CoinFragment : Fragment() {

    private lateinit var root: View

    private val args: CoinFragmentArgs by navArgs()

    private val viewModel: CoinViewModel by lazy {
        ViewModelProviders.of(this, CoinViewModel.Factory(args.id, "m1"))
                .get(CoinViewModel::class.java)
    }

    private lateinit var chart: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.root = inflater.inflate(R.layout.fragment_coin, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = "${args.name}(${args.symbol})"
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        root.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).setOnRefreshListener {
            viewModel.coin.refresh()
            viewModel.chartData.refresh()
        }

        isLoading = true
        viewModel.coin.observe(this, Observer {
            isLoading = false
            it?.let { onDataLoaded(it) } ?: onLoadFailed()
        })


        chart = root.findViewById(R.id.chart1) as LineChart
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        //chart.setBackgroundColor(Color.TRANSPARENT)

        viewModel.chartData.observe(viewLifecycleOwner, Observer {
            it?.apply {
                onChartDataLoaded(it)
            } ?: toast("Cannot load chart")
        })

        root.findViewById<TabLayout>(R.id.tabLayout).apply {
            addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {
                    // nothing
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    // nothing
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    android.util.Log.d("CoinFragment", "onTabSelected")
                    viewModel.chartData.setInterval(tab.text.toString())
                }
            })
        }

        return this.root
    }

    private fun onChartDataLoaded(data: List<ChartPoint>) {
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
                //setLabelCount(5, false)
                //isGranularityEnabled = true
            }
            //axisLeft.setDrawTopYLabelEntry(true)
            //description.isEnabled = true

            axisRight.isEnabled = false

            // add data
            setChartData(data)

            legend.isEnabled = false

            animateXY(0, 500)

            // don't forget to refresh the drawing
            invalidate()
        }
    }

    private fun setChartData(chartData: List<ChartPoint>) {
        val values = chartData.map { Entry(it.time.toFloat(), it.price.toFloat()) }

        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")

            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(false)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            //set1.setCircleColor(Color.WHITE)
            //set1.highLightColor = Color.rgb(244, 117, 117)
            val accentColor = fetchAccentColor()
            set1.color = accentColor//fetchAccentColor()//ContextCompat.getColor(requireContext(), R.color.colorAccent)
            set1.fillColor = accentColor//Color.WHITE
            //set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            //set1.fillFormatter = IFillFormatter { _, _ -> -10f }

            // create a data object with the datasets
            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setDrawValues(true)

            // set data
            chart.data = data
        }
    }

    private fun fetchAccentColor(): Int {
        val typedValue = TypedValue()

        val a = requireContext().obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)

        a.recycle()

        return color
    }

    private var isLoading: Boolean = true
        set(value) {
            root.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).isRefreshing = value
            field = value
        }

    private fun onDataLoaded(coin: Coin) {
        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = coin.name
            subtitle = coin.symbol
        }

        var isFavorite = PreferenceHelper.isFavorite(coin)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        fab.hide()
        fab.setImageDrawable(requireContext().getDrawable(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border))
        fab.setOnClickListener {
            if (!isFavorite) {
                PreferenceHelper.addFavorite(coin)
                toast("Added to favorites")
            } else {
                PreferenceHelper.removeFavorite(coin)
                toast("Removed from favorites")
            }
            isFavorite = !isFavorite
            fab.setImageDrawable(requireContext().getDrawable(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border))
        }
        fab.visibility = View.VISIBLE
        fab.show()

        currencySymbol = CurrencySymbolResolver.resolve(requireContext(), coin.currency)

        root.findViewById<TextView>(R.id.name).text = coin.name
        root.findViewById<TextView>(R.id.symbol).text = coin.symbol
        root.findViewById<TextView>(R.id.rank).text = getString(R.string.rank_value, coin.rank)
        root.findViewById<TextView>(R.id.price_toolbar)?.showPrice(coin.price)
        root.findViewById<TextView>(R.id.price)?.showPrice(coin.price)
        root.findViewById<TextView>(R.id.change_toolbar)?.showChange(coin.percentChange24h)
        root.findViewById<TextView>(R.id.percent_change_1h)?.showChange(coin.percentChange1h)
        root.findViewById<TextView>(R.id.percent_change_24h)?.showChange(coin.percentChange24h)
        root.findViewById<TextView>(R.id.percent_change_7d)?.showChange(coin.percentChange7d)
        root.findViewById<TextView>(R.id.marketCap)?.showPrice(coin.marketCap)
        root.findViewById<TextView>(R.id.available_supply)?.showPrice(coin.availableSupply)
        root.findViewById<TextView>(R.id.max_supply)?.showPrice(coin.maxSupply)
        root.findViewById<TextView>(R.id.volume_24h)?.showPrice(coin.volume24h)
        root.findViewById<TextView>(R.id.average_price_24h)?.showPrice(coin.averagePrice24h)
        root.findViewById<TextView>(R.id.lastUpdated)?.text = coin.lastUpdated?.toRelativeTimeSpan()
    }

    private val changeFormatter = DecimalFormat("#.##")

    private lateinit var currencySymbol: String

    private fun TextView.showPrice(priceRep: String?) {
        val price = priceRep?.toDoubleOrNull()
        if (price == null) {
            this.text = "-"
        } else {
            val priceFormatter = DecimalFormat(if (price.toDouble() < 1) "#.########" else ".##")
            text = getString(R.string.price, currencySymbol, priceFormatter.format(price))
        }
    }

    private fun TextView.showChange(change: String?) {
        if (change == null) {
            text = "-"
        } else {
            val t: String = change.toDoubleOrNull()?.run { changeFormatter.format(this) } ?: "-"
            text = context.getString(R.string.percent_change, t)
            val positiveColor = ContextCompat.getColor(requireContext(), R.color.positive_color)
            val negativeColor = ContextCompat.getColor(requireContext(), R.color.negative_color)
            setTextColor(when {
                change.contains("-") -> negativeColor
                else -> positiveColor
            })
        }
    }

    private fun onLoadFailed() {
        toast("Failure")
    }

    private fun String.toRelativeTimeSpan(): CharSequence =
            DateUtils.getRelativeTimeSpanString(requireContext(), this.toLong() * 1000)
}

