package com.ibashkimi.cryptomarket

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.ibashkimi.cryptomarket.livedata.FavoriteCoinsViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.CoinIconUrlResolver

class FavoriteFragment : androidx.fragment.app.Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var adapter: CoinAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val viewModel: FavoriteCoinsViewModel by lazy {
        ViewModelProviders.of(this).get(FavoriteCoinsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        val recyclerView = root.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(object : ImageLoader {
            override fun loadImage(coin: Coin, imageView: ImageView) {
                Glide.with(imageView.context).load(CoinIconUrlResolver.resolve(coin)).into(imageView)
            }
        }, object : OnCoinClickedListener {
            override fun onCoinClicked(coin: Coin) {
                mainNavController.navigate(HomeFragmentDirections.actionMainToCoin(coin.id, coin.name, coin.symbol))
            }
        })
        recyclerView.adapter = adapter

        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener { refresh() }

        isLoading = true
        viewModel.coins.observe(this, Observer {
            isLoading = false
            //adapter.submitList(it) // todo
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
        if (PreferenceHelper.KEY_CURRENCY == key || PreferenceHelper.KEY_FAVORITE_COINS == key)
            refresh()
    }

    private val mainNavController: NavController
        get() = requireActivity().findNavController(R.id.main_nav_host_fragment)

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