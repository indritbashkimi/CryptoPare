package com.ibashkimi.cryptomarket

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibashkimi.cryptomarket.livedata.CoinViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.CurrencySymbolResolver
import com.ibashkimi.cryptomarket.utils.priceFormat
import com.ibashkimi.cryptomarket.utils.toast
import java.text.DecimalFormatSymbols


class CoinFragment : Fragment() {

    private lateinit var root: View

    private val args: CoinFragmentArgs by navArgs()

    private val viewModel: CoinViewModel by lazy {
        ViewModelProviders.of(this, CoinViewModel.Factory(args.id))
                .get(CoinViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.root = inflater.inflate(R.layout.fragment_coin, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = "${args.name}(${args.symbol})"
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.chart_fragment, ChartFragment.newInstance(intent.extras.getString("symbol")), "chart")
                    .commit()
        }*/

        /*Glide.with(this@CoinFragment)
                .load(CoinIconUrlResolver.resolve(intent.extras.getString("symbol")))
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        toolbar.logo = resource
                    }
                })*/

        root.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).setOnRefreshListener {
            viewModel.coin.refresh()
        }

        isLoading = true
        viewModel.coin.observe(this, Observer {
            isLoading = false
            it?.let { onDataLoaded(it) } ?: onLoadFailed()
        })

        return this.root
    }

    private var isLoading: Boolean = true
        set(value) {
            root.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).isRefreshing = value
            field = value
        }

    private fun onDataLoaded(coin: Coin) {
        val currencySymbol = CurrencySymbolResolver.resolve(requireContext(), coin.currency)
        val decimalFormatSymbols = DecimalFormatSymbols()
        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = "${coin.name}(${coin.symbol})"
            //subtitle = coin.symbol
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

        root.findViewById<TextView>(R.id.name).text = coin.name
        root.findViewById<TextView>(R.id.symbol).text = coin.symbol
        root.findViewById<TextView>(R.id.rank).text = getString(R.string.rank_value, coin.rank)

        root.findViewById<TextView>(R.id.price_toolbar).text = getString(R.string.price, currencySymbol, coin.price?.priceFormat(decimalFormatSymbols))

        root.findViewById<TextView>(R.id.price).text = getString(R.string.price, currencySymbol, coin.price?.priceFormat(decimalFormatSymbols))

        val lastUpdated = root.findViewById<TextView>(R.id.lastUpdated)
        lastUpdated.text = coin.lastUpdated?.toRelativeTimeSpan()

        root.findViewById<TextView>(R.id.percent_change_1h).text = getString(R.string.percent_change, coin.percentChange1h)
        root.findViewById<TextView>(R.id.percent_change_24h).text = getString(R.string.percent_change, coin.percentChange24h)
        root.findViewById<TextView>(R.id.percent_change_7d).text = getString(R.string.percent_change, coin.percentChange7d)
        root.findViewById<TextView>(R.id.marketCap).text = getString(R.string.price, currencySymbol, coin.marketCap?.priceFormat(decimalFormatSymbols))
        root.findViewById<TextView>(R.id.circulatingSupply).text = coin.availableSupply?.priceFormat(decimalFormatSymbols)
        root.findViewById<TextView>(R.id.volume_24h).text = getString(R.string.price, currencySymbol, coin.volume24h?.priceFormat(decimalFormatSymbols))
        root.findViewById<TextView>(R.id.max_supply).text = coin.maxSupply?.priceFormat(decimalFormatSymbols)
        root.findViewById<TextView>(R.id.totalSupply).text = coin.totalSupply?.priceFormat(decimalFormatSymbols)
    }

    private fun onLoadFailed() {
        toast("Failure")
    }

    private fun String.toRelativeTimeSpan(): CharSequence =
            DateUtils.getRelativeTimeSpanString(requireContext(), this.toLong() * 1000)
}

