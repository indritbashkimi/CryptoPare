package com.ibashkimi.cryptomarket.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.ibashkimi.cryptomarket.HomeFragmentDirections
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.coinlist.CoinViewHolder
import com.ibashkimi.cryptomarket.coinlist.OnCoinClickedListener
import com.ibashkimi.cryptomarket.model.Coin

class FavoriteFragment : Fragment(), OnCoinClickedListener {

    private lateinit var adapter: Adapter

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val viewModel: FavoriteCoinsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).setTitle(R.string.page_favorite)
        root.findViewById<AppBarLayout>(R.id.appBar).isLiftOnScroll = true

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        //val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        //recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = Adapter()
        recyclerView.adapter = adapter

        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener { refresh() }

        setIsLoading(true)
        viewModel.coins.observe(viewLifecycleOwner, Observer {
            setIsLoading(false)
            if (it == null) {
                onLoadFailed()
            } else {
                onDataReady(it)
            }
        })
        return root
    }

    override fun onCoinClicked(coin: Coin) {
        mainNavController.navigate(HomeFragmentDirections.actionMainToCoin(coin.id, coin.name, coin.symbol))
    }

    private val mainNavController: NavController
        get() = requireActivity().findNavController(R.id.main_nav_host_fragment)

    private fun setIsLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    private fun onDataReady(data: List<Coin>) {
        adapter.data.clear()
        adapter.data.addAll(data)
        adapter.notifyDataSetChanged()
    }

    private fun onLoadFailed() {
        Toast.makeText(requireContext(), "Load error", Toast.LENGTH_SHORT).show()
    }

    private fun refresh() {
        setIsLoading(true)
        viewModel.refresh()
    }

    inner class Adapter : RecyclerView.Adapter<CoinViewHolder>() {

        val data = ArrayList<Coin>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
            return CoinViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_crypto_5, parent, false), this@FavoriteFragment)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
            val coin: Coin? = data[position]
            if (coin == null)
                holder.clear()
            else
                holder.bind(coin, null)
        }

    }
}