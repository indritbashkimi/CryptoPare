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
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = "${coin.name}(${coin.symbol})"
            //subtitle = coin.symbol
            Picasso.with(this@CoinActivity).load(CoinIconUrlResolver.resolve(coin)).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {

                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                    logo = BitmapDrawable(resources, bitmap)
                }
            })
        }
        val rank = findViewById<TextView>(R.id.rank)
        rank.text = getString(R.string.rank_value, coin.rank)

        val price = findViewById<TextView>(R.id.price)
        price.text = getString(R.string.price, CurrencySymbolResolver.resolve(this, coin.currency), coin.price)

        val lastUpdated = findViewById<TextView>(R.id.lastUpdated)
        lastUpdated.text = coin.lastUpdated?.toRelativeTimeSpan()
    }

    private fun onLoadFailed() {
        Toast.makeText(this@CoinActivity, "Failure", Toast.LENGTH_SHORT).show()
    }

    private fun String.toRelativeTimeSpan(): CharSequence =
            DateUtils.getRelativeTimeSpanString(this@CoinActivity, this.toLong() * 1000)
}
