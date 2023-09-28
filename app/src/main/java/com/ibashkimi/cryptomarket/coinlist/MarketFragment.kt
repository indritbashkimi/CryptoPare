package com.ibashkimi.cryptomarket.coinlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibashkimi.cryptomarket.HomeFragmentDirections
import com.ibashkimi.cryptomarket.R

class MarketFragment : Fragment() {

    private lateinit var adapter: CoinAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val viewModel: CoinsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        //val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        //recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(null) {
            navController.navigate(HomeFragmentDirections.actionMainToCoin(it.id))
        }
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

        val errorView = root.findViewById<View>(R.id.error)

        isLoading = true
        viewModel.coins.observe(viewLifecycleOwner) {
            isLoading = false
            errorView.isVisible = false
            adapter.submitList(it)
            if (it == null) {
                errorView.isVisible = true
            }
        }
        return root
    }

    private var isLoading: Boolean = true
        set(value) {
            swipeRefresh.isRefreshing = value
            field = value
        }

    private fun refresh() {
        adapter.currentList?.dataSource?.invalidate()
        isLoading = true
    }

}