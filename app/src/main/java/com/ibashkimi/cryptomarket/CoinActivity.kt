package com.ibashkimi.cryptomarket

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.format.DateUtils
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.ibashkimi.cryptomarket.livedata.CoinViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.CurrencySymbolResolver
import com.ibashkimi.cryptomarket.utils.priceFormat
import com.ibashkimi.cryptomarket.utils.toast
import java.text.DecimalFormatSymbols


class CoinActivity : AppCompatActivity() {

    private val viewModel: CoinViewModel by lazy {
        ViewModelProviders.of(this, CoinViewModel.Factory(intent.extras.getString("id")!!))
                .get(CoinViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "${intent.extras.getString("name")}(${intent.extras.getString("symbol")})"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.chart_fragment, ChartFragment.newInstance(intent.extras.getString("symbol")), "chart")
                    .commit()
        }*/

        /*Glide.with(this@CoinActivity)
                .load(CoinIconUrlResolver.resolve(intent.extras.getString("symbol")))
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        toolbar.logo = resource
                    }
                })*/

        findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).setOnRefreshListener {
            viewModel.coin.refresh()
        }

        isLoading = true
        viewModel.coin.observe(this, Observer {
            isLoading = false
            it?.let { onDataLoaded(it) } ?: onLoadFailed()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private var isLoading: Boolean = true
        set(value) {
            findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).isRefreshing = value
            field = value
        }

    private fun onDataLoaded(coin: Coin) {
        val currencySymbol = CurrencySymbolResolver.resolve(this, coin.currency)
        val decimalFormatSymbols = DecimalFormatSymbols()
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = "${coin.name}(${coin.symbol})"
            //subtitle = coin.symbol
        }

        var isFavorite = PreferenceHelper.isFavorite(coin)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.hide()
        fab.setImageDrawable(getDrawable(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border))
        fab.setOnClickListener {
            if (!isFavorite) {
                PreferenceHelper.addFavorite(coin)
                toast("Added to favorites")
            } else {
                PreferenceHelper.removeFavorite(coin)
                toast("Removed from favorites")
            }
            isFavorite = !isFavorite
            fab.setImageDrawable(getDrawable(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border))
        }
        fab.visibility = View.VISIBLE
        fab.show()

        findViewById<TextView>(R.id.name).setText(coin.name)
        findViewById<TextView>(R.id.symbol).setText(coin.symbol)
        findViewById<TextView>(R.id.rank).setText(getString(R.string.rank_value, coin.rank))

        findViewById<TextView>(R.id.price_toolbar).text = getString(R.string.price, currencySymbol, coin.price?.priceFormat(decimalFormatSymbols))

        findViewById<TextView>(R.id.price).text = getString(R.string.price, currencySymbol, coin.price?.priceFormat(decimalFormatSymbols))

        val lastUpdated = findViewById<TextView>(R.id.lastUpdated)
        lastUpdated.text = coin.lastUpdated?.toRelativeTimeSpan()

        findViewById<TextView>(R.id.percent_change_1h).setText(getString(R.string.percent_change, coin.percentChange1h))
        findViewById<TextView>(R.id.percent_change_24h).setText(getString(R.string.percent_change, coin.percentChange24h))
        findViewById<TextView>(R.id.percent_change_7d).setText(getString(R.string.percent_change, coin.percentChange7d))
        findViewById<TextView>(R.id.marketCap).setText(getString(R.string.price, currencySymbol, coin.marketCap?.priceFormat(decimalFormatSymbols)))
        findViewById<TextView>(R.id.circulatingSupply).setText(coin.availableSupply?.priceFormat(decimalFormatSymbols))
        findViewById<TextView>(R.id.volume_24h).setText(getString(R.string.price, currencySymbol, coin.volume24h?.priceFormat(decimalFormatSymbols)))
        findViewById<TextView>(R.id.max_supply).setText(coin.maxSupply?.priceFormat(decimalFormatSymbols))
        findViewById<TextView>(R.id.totalSupply).setText(coin.totalSupply?.priceFormat(decimalFormatSymbols))
    }

    private fun onLoadFailed() {
        toast("Failure")
    }

    private fun String.toRelativeTimeSpan(): CharSequence =
            DateUtils.getRelativeTimeSpanString(this@CoinActivity, this.toLong() * 1000)
}

