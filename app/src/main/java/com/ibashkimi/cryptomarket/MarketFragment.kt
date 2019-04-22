package com.ibashkimi.cryptomarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibashkimi.cryptomarket.livedata.CoinsViewModel
import com.ibashkimi.cryptomarket.model.Coin


class MarketFragment : Fragment() {

    private lateinit var adapter: CoinAdapter

    private lateinit var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    private val viewModel: CoinsViewModel by lazy {
        ViewModelProviders.of(this).get(CoinsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_market, container, false)

        val navController = requireActivity().findNavController(R.id.main_nav_host_fragment)

        root.findViewById<AppBarLayout>(R.id.appBar).isLiftOnScroll = true
        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.app_name)
            inflateMenu(R.menu.main)
            setNavigationOnClickListener {
                navController.navigate(R.id.action_main_to_search)
            }
            setOnMenuItemClickListener {
                NavigationUI.onNavDestinationSelected(it, navController)
            }
        }

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(null, object : OnCoinClickedListener {
            override fun onCoinClicked(coin: Coin) {
                navController.navigate(HomeFragmentDirections
                        .actionMainToCoin(coin.id, coin.name, coin.symbol))
            }
        })
        recyclerView.adapter = adapter

        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener { refresh() }

        val actionButton = root.findViewById<FloatingActionButton>(R.id.fab)
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
        viewModel.coins.observe(viewLifecycleOwner, Observer {
            isLoading = false
            adapter.submitList(it)
            if (it == null)
                onLoadFailed()
        })
        return root
    }

    private var isLoading: Boolean = true
        set(value) {
            swipeRefresh.isRefreshing = value
            field = value
        }

    private fun onLoadFailed() {
        Toast.makeText(requireContext(), "Load error", Toast.LENGTH_SHORT).show()
    }

    private fun refresh() {
        viewModel.refresh()
        isLoading = true
    }

}