package com.ibashkimi.cryptomarket

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.ibashkimi.cryptomarket.livedata.CoinsViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.CoinIconUrlResolver


class MarketFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var adapter: CoinAdapter

    private lateinit var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    private val viewModel: CoinsViewModel by lazy {
        ViewModelProviders.of(this).get(CoinsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_market, container, false)

        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.main)
        toolbar.setNavigationOnClickListener {
            mainNavController.navigate(R.id.action_main_to_search)
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                android.R.id.home -> {
                    mainNavController.navigate(R.id.action_main_to_search)
                    true
                }
                R.id.action_refresh -> {
                    refresh()
                    true
                }
                R.id.action_currency -> {
                    val currencies = resources.getStringArray(R.array.currencies)
                    AlertDialog.Builder(requireContext())
                            .setSingleChoiceItems(currencies, currencies.indexOf(PreferenceHelper.currency)
                            ) { dialog, which ->
                                PreferenceHelper.currency = currencies[which]
                                dialog.dismiss()
                            }
                            .create().show()
                    true
                }
                R.id.action_settings -> {
                    mainNavController.navigate(R.id.action_main_to_settings)
                    true
                }
                R.id.action_about -> {
                    mainNavController.navigate(R.id.action_main_to_about)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
        root.findViewById<AppBarLayout>(R.id.appBar).isLiftOnScroll = true

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(object : CoinAdapter.ImageLoader {
            override fun loadImage(coin: Coin, imageView: ImageView) {
                Glide.with(imageView.context).load(CoinIconUrlResolver.resolve(coin)).into(imageView)
            }
        }, object : CoinAdapter.OnCoinClicked {
            override fun onCoinClicked(coin: Coin) {
                mainNavController.navigate(HomeFragmentDirections.actionMainToCoin(coin.id, coin.name, coin.symbol))
            }
        })
        recyclerView.adapter = adapter

        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener { refresh() }

        val actionButton = root.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
        actionButton.setOnClickListener {
            recyclerView.scrollToPosition(0)
        }

        actionButton.post { actionButton.hide() }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && actionButton.isShown) {
                    actionButton.hide()
                } else if (dy > 0 && !actionButton.isShown) {
                    actionButton.show()
                }
            }
        })

        isLoading = true
        viewModel.coins.observe(this, Observer {
            isLoading = false
            adapter.submitList(it)
            if (it == null)
                onLoadFailed()
        })
        return root
    }

    override fun onStart() {
        super.onStart()
        PreferenceHelper.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        PreferenceHelper.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (PreferenceHelper.KEY_CURRENCY == key)
            refresh()
    }

    private val mainNavController: NavController
        get() = requireActivity().findNavController(R.id.main_nav_host_fragment)

    private val homeNavController: NavController
        get() = findNavController()


    private var isLoading: Boolean = true
        set(value) {
            swipeRefresh.isRefreshing = value
            field = value
        }

    private fun onLoadFailed() {
        Toast.makeText(requireContext(), "Load error", Toast.LENGTH_SHORT).show()
    }

    fun refresh() {
        if (isAdded) {
            viewModel.refresh()
            isLoading = true
        }
    }

}