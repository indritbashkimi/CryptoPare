package com.ibashkimi.cryptomarket

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
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

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        val pager = findViewById<ViewPager>(R.id.pager)
        pager.adapter = pagerAdapter

        findViewById<TabLayout>(R.id.tabs).setupWithViewPager(pager)
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

    inner class PagerAdapter(private val fragmentManger: FragmentManager) : FragmentPagerAdapter(fragmentManger) {
        override fun getCount(): Int = PAGES_COUNT

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> {
                fragmentManger.findFragmentByTag("frag_at_$position") ?: MarketFragment()
            }
            1 -> {
                fragmentManger.findFragmentByTag("frag_at_$position") ?: FavoriteFragment()
            }
            else -> {
                throw IllegalArgumentException("Invalid position $position.")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> this@MainActivity.getString(R.string.page_all)
                1 -> this@MainActivity.getString(R.string.page_favorite)
                else -> throw IllegalArgumentException("Invalid position $position.")
            }
        }
    }

    companion object {
        const val MARKET_FRAGMENT_TAG = "market_fragment"
        const val PAGES_COUNT = 2
    }
}