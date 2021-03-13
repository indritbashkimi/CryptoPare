package com.ibashkimi.cryptomarket.coininfo

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.databinding.FragmentCoinBinding
import com.ibashkimi.cryptomarket.model.ChartInterval
import com.ibashkimi.cryptomarket.model.ChartPoint
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.model.HistoryKey
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.toast
import java.text.DecimalFormat
import java.util.*

class CoinFragment : Fragment() {

    private val args: CoinFragmentArgs by navArgs()

    private val viewModel: CoinViewModel by viewModels()

    private var _binding: FragmentCoinBinding? = null

    private val binding: FragmentCoinBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinBinding.inflate(inflater, container, false)

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.historyChart.setViewPortOffsets(0f, 0f, 0f, 0f)
        //chart.setBackgroundColor(Color.TRANSPARENT)

        binding.tabLayout.apply {
            viewModel.historyKeys.forEach {
                addTab(it.chartInterval.textResId, it)
            }
            onTabSelected {
                viewModel.historyKey.value = tag as HistoryKey
            }
            //selectTab(getTabAt(0)) // this sets historyKey todo use bundle to save selected tab on rotation
        }

        viewModel.coinId.value = args.coinId
        viewModel.historyKey.value = viewModel.historyKeys.firstOrNull()

        isLoading = true

        viewModel.coin.observe(viewLifecycleOwner, Observer {
            isLoading = false
            it?.let { onDataLoaded(it) } ?: onLoadFailed()
        })

        viewModel.history.observe(viewLifecycleOwner, Observer {
            it?.apply {
                onChartDataLoaded(it)
            } ?: toast("Cannot load chart")
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onChartDataLoaded(data: List<ChartPoint>) {
        binding.historyChart.apply {
            configureChart()

            setChartData(data)

            animateXY(0, 500)
            // invalidate() not necessary because animate invalidates it
        }
    }

    private fun setChartData(chartData: List<ChartPoint>) {
        val values = chartData.map { Entry(it.time.toFloat(), it.price.toFloat()) }

        val set1: LineDataSet
        val chart = binding.historyChart
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")

            set1.configureDataSet(binding.historyChart)

            // create a data object with the datasets
            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setDrawValues(true)

            // set data
            chart.data = data
        }
    }

    private var isLoading: Boolean = true
        set(value) {
            binding.swipeRefresh.isRefreshing = value
            field = value
        }

    private fun onDataLoaded(coin: Coin) {
        binding.toolbar.apply {
            title = coin.name
            subtitle = coin.symbol
        }

        var isFavorite = PreferenceHelper.isFavorite(coin)
        val fab = binding.fab
        fab.hide()
        fab.setImageDrawable(
            ContextCompat.getDrawable(
                fab.context,
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        )
        fab.setOnClickListener {
            if (!isFavorite) {
                PreferenceHelper.addFavorite(coin)
                toast("Added to favorites")
            } else {
                PreferenceHelper.removeFavorite(coin)
                toast("Removed from favorites")
            }
            isFavorite = !isFavorite
            fab.setImageDrawable(
                ContextCompat.getDrawable(
                    fab.context,
                    if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                )
            )
        }
        fab.visibility = View.VISIBLE
        fab.show()

        currencySymbol = PreferenceHelper.currencySymbol ?: PreferenceHelper.currencyName

        binding.apply {
            name.text = coin.name
            symbol.text = coin.symbol
            rank.text = getString(R.string.rank_value, coin.rank)
            priceToolbar.showPrice(coin.price)
            price.showPrice(coin.price)
            changeToolbar.showChange(coin.percentChange24h)
            percentChange1h.showChange(coin.percentChange1h)
            percentChange24h.showChange(coin.percentChange24h)
            percentChange7d.showChange(coin.percentChange7d)
            marketCap.showPrice(coin.marketCap)
            availableSupply.showPrice(coin.availableSupply)
            maxSupply.showPrice(coin.maxSupply)
            volume24h.showPrice(coin.volume24h)
            averagePrice24h.showPrice(coin.averagePrice24h)
            //lastUpdated.text = coin.lastUpdated?.toRelativeTimeSpan()
        }
    }

    private val changeFormatter = DecimalFormat("#.##")

    private var currencySymbol: String? = null

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
            setTextColor(
                when {
                    change.contains("-") -> negativeColor
                    else -> positiveColor
                }
            )
        }
    }

    private fun onLoadFailed() {
        toast("Failure")
    }

    private fun String.toRelativeTimeSpan(): CharSequence =
        this.toLong().toRelativeTimeSpan()

    private fun Long.toRelativeTimeSpan(): CharSequence =
        DateUtils.getRelativeTimeSpanString(requireContext(), this * 1000)

    private fun Long.asDateString(): CharSequence =
        Date(this).toString()

    private val ChartInterval.textResId: Int
        get() {
            return when (this) {
                ChartInterval.ALL -> R.string.chart_all
                ChartInterval.DAY -> R.string.chart_day
                ChartInterval.DAY2 -> R.string.chart_2_days
                ChartInterval.WEEK -> R.string.chart_week
                ChartInterval.WEEK2 -> R.string.chart_2_weeks
                ChartInterval.WEEK3 -> R.string.chart_3_weeks
                ChartInterval.MONTH -> R.string.chart_month
                ChartInterval.MONTH2 -> R.string.chart_2_months
                ChartInterval.MONTH3 -> R.string.chart_3_months
                ChartInterval.MONTH6 -> R.string.chart_6_months
                ChartInterval.YEAR -> R.string.chart_year
                ChartInterval.YEAR2 -> R.string.chart_2_years
            }
        }
}

