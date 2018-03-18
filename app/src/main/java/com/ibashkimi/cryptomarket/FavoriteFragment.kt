package com.ibashkimi.cryptomarket

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ibashkimi.cryptomarket.livedata.FavoriteCoinsViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.CoinIconUrlResolver

class FavoriteFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var adapter: CoinAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val viewModel: FavoriteCoinsViewModel by lazy {
        ViewModelProviders.of(this).get(FavoriteCoinsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(object : CoinAdapter.ImageLoader {
            override fun loadImage(coin: Coin, imageView: ImageView) {
                Glide.with(imageView.context).load(CoinIconUrlResolver.resolve(coin)).into(imageView)
            }
        })
        recyclerView.adapter = adapter

        swipeRefresh = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener { refresh() }

        isLoading = true
        viewModel.coins.observe(this, Observer {
            isLoading = false
            adapter.submitList(it)
            if (it == null)
                onLoadFailed()
        })
        return root
    }

    override fun onResume() {
        super.onResume()
        PreferenceHelper.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceHelper.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (PreferenceHelper.KEY_CURRENCY == key)
            refresh()
    }


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