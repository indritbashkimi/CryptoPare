package com.ibashkimi.cryptomarket

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.ibashkimi.cryptomarket.about.AboutActivity
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.settings.SettingsActivity


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MarketFragment(), MARKET_FRAGMENT_TAG)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            R.id.action_refresh -> {
                val fragment = supportFragmentManager.findFragmentByTag(MARKET_FRAGMENT_TAG)
                        as MarketFragment?
                fragment?.refresh()
                true
            }
            R.id.action_currency -> {
                val currencies = resources.getStringArray(R.array.currencies)
                AlertDialog.Builder(this)
                        .setSingleChoiceItems(currencies, currencies.indexOf(PreferenceHelper.currency),
                                { dialog, which ->
                                    PreferenceHelper.currency = currencies[which]
                                    dialog.dismiss()
                                })
                        .create().show()
                true
            }
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

    companion object {
        const val MARKET_FRAGMENT_TAG = "market_fragment"
    }
}