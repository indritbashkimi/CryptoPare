package com.ibashkimi.cryptomarket.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.ibashkimi.cryptomarket.HomeFragmentDirections
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.coinlist.CoinViewHolder
import com.ibashkimi.cryptomarket.model.Coin
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class FavoriteFragment : Fragment() {

    private lateinit var adapter: Adapter

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val viewModel: FavoriteCoinsViewModel by viewModels()

    private fun SwipeRefreshLayout.refreshes(): Flow<Unit> = callbackFlow {
        this@refreshes.setOnRefreshListener {
            trySend(Unit)
        }
        awaitClose { this@refreshes.setOnRefreshListener(null) }
    }

    private fun View.clicks(): Flow<Unit> = callbackFlow {
        this@clicks.setOnClickListener {
            this.trySend(Unit)
        }
        awaitClose { this@clicks.setOnClickListener(null) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        root.findViewById<Button>(R.id.fab).clicks()
        root.findViewById<Toolbar>(R.id.toolbar).setTitle(R.string.page_favorite)
        root.findViewById<AppBarLayout>(R.id.appBar).isLiftOnScroll = true

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = Adapter {
            navController.navigate(HomeFragmentDirections.actionMainToCoin(it.id))
        }
        recyclerView.adapter = adapter

        swipeRefresh = root.findViewById(R.id.swipeRefresh)

        swipeRefresh.refreshes().map { refresh() }.launchIn(lifecycleScope)

        setIsLoading(true)

        viewModel.coins.observe(viewLifecycleOwner) {
            setIsLoading(false)
            if (it == null) {
                onLoadFailed()
            } else {
                onDataReady(it)
            }
        }

        return root
    }

    private val navController: NavController
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

    class Adapter(private val onClick: (Coin) -> Unit) : RecyclerView.Adapter<CoinViewHolder>() {

        val data = ArrayList<Coin>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
            return CoinViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_crypto_5,
                    parent,
                    false
                ), onClick
            )
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