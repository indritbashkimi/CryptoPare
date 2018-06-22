package com.ibashkimi.cryptomarket

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ibashkimi.cryptomarket.livedata.CoinsViewModel
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.settings.PreferenceHelper
import com.ibashkimi.cryptomarket.utils.CoinIconUrlResolver


class MarketFragment : androidx.fragment.app.Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var adapter: CoinAdapter

    private lateinit var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    private val viewModel: CoinsViewModel by lazy {
        ViewModelProviders.of(this).get(CoinsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_market, container, false)
        val recyclerView = root.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(object : CoinAdapter.ImageLoader {
            override fun loadImage(coin: Coin, imageView: ImageView) {
                Glide.with(imageView.context).load(CoinIconUrlResolver.resolve(coin)).into(imageView)
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
        recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
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