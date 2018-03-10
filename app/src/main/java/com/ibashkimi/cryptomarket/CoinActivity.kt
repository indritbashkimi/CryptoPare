package com.ibashkimi.cryptomarket

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin

class CoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "${intent.extras.getString("title")}(${intent.extras.getString("symbol")})"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        DataManager.getCoin(intent.action!!,
                onSuccess = {
                    onDataLoaded(it)
                },
                onFailure = {
                    onLoadFailed()
                })
    }

    private fun onDataLoaded(coin: Coin) {
        Toast.makeText(this@CoinActivity, "Success", Toast.LENGTH_SHORT).show()
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = "${coin.name}(${coin.symbol})"
            //subtitle = coin.symbol
        }
    }

    private fun onLoadFailed() {
        Toast.makeText(this@CoinActivity, "Failure", Toast.LENGTH_SHORT).show()
    }
}
