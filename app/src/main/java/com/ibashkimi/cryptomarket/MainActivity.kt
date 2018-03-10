package com.ibashkimi.cryptomarket

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.ibashkimi.cryptomarket.about.AboutActivity
import com.ibashkimi.cryptomarket.livedata.CoinsViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.SettingsActivity
import com.ibashkimi.cryptomarket.utils.CoinIconUrlResolver
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: CoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(object : CoinAdapter.ImageLoader {
            override fun loadImage(coin: Coin, imageView: ImageView) {
                Picasso.with(imageView.context).load(CoinIconUrlResolver.resolve(coin)).into(imageView)
            }
        })
        recyclerView.adapter = adapter

        val viewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
        viewModel.coins.observe(this, Observer {
            findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).isRefreshing = false
            it?.apply { onDataLoaded(this) } ?: onLoadFailed()
        })

        findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).setOnRefreshListener {
            viewModel.coins.refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onDataLoaded(data: List<Coin>) {
        adapter.updateData(data)
    }

    private fun onLoadFailed() {
        Toast.makeText(this@MainActivity, "Load error", Toast.LENGTH_SHORT).show()
    }
}