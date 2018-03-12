package com.ibashkimi.cryptomarket

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.format.DateUtils
import android.widget.TextView
import android.widget.Toast
import com.ibashkimi.cryptomarket.livedata.CoinViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.utils.CoinIconUrlResolver
import com.ibashkimi.cryptomarket.utils.CurrencySymbolResolver
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class CoinActivity : AppCompatActivity() {

    private val viewModel: CoinViewModel by lazy {
        ViewModelProviders.of(this, CoinViewModel.Factory(intent.action))
                .get(CoinViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "${intent.extras.getString("name")}(${intent.extras.getString("symbol")})"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Picasso.with(this@CoinActivity).load(CoinIconUrlResolver.resolve(intent.extras.getString("symbol"))).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                toolbar.logo = BitmapDrawable(resources, bitmap)
            }
        })

        findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).setOnRefreshListener {
            viewModel.coin.refresh()
        }

        isLoading = true
        viewModel.coin.observe(this, Observer {
            isLoading = false
            it?.let { onDataLoaded(it) } ?: onLoadFailed()
        })
    }

    private var isLoading: Boolean = true
        set(value) {
            findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).isRefreshing = value
            field = value
        }

    private fun onDataLoaded(coin: Coin) {
        val currencySymbol = CurrencySymbolResolver.resolve(this, coin.currency)
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = "${coin.name}(${coin.symbol})"
            //subtitle = coin.symbol
        }
        findViewById<TextView>(R.id.name).setText(coin.name)
        findViewById<TextView>(R.id.symbol).setText(coin.symbol)
        findViewById<TextView>(R.id.rank).setText(getString(R.string.rank_value, coin.rank))

        val price = findViewById<TextView>(R.id.price)
        price.text = getString(R.string.price, currencySymbol, coin.price)

        findViewById<TextView>(R.id.priceBtc).setText(getString(R.string.price, getString(R.string.btc_symbol), coin.priceBtc))

        val lastUpdated = findViewById<TextView>(R.id.lastUpdated)
        lastUpdated.text = coin.lastUpdated?.toRelativeTimeSpan()

        findViewById<TextView>(R.id.percent_change_1h).setText(getString(R.string.percent_change, coin.percentChange1h))
        findViewById<TextView>(R.id.percent_change_24h).setText(getString(R.string.percent_change, coin.percentChange24h))
        findViewById<TextView>(R.id.percent_change_7d).setText(getString(R.string.percent_change, coin.percentChange7d))
        findViewById<TextView>(R.id.marketCap).setText(getString(R.string.price, currencySymbol, coin.marketCap))
        findViewById<TextView>(R.id.circulatingSupply).setText(coin.availableSupply)
        findViewById<TextView>(R.id.volume_24h).setText(getString(R.string.price, currencySymbol, coin.volume24h))
        findViewById<TextView>(R.id.max_supply).setText(coin.maxSupply)
        findViewById<TextView>(R.id.totalSupply).setText(coin.totalSupply)
    }

    private fun onLoadFailed() {
        Toast.makeText(this@CoinActivity, "Failure", Toast.LENGTH_SHORT).show()
    }

    private fun String.toRelativeTimeSpan(): CharSequence =
            DateUtils.getRelativeTimeSpanString(this@CoinActivity, this.toLong() * 1000)
}

