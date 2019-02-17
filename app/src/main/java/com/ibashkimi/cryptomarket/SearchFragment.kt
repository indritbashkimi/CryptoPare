package com.ibashkimi.cryptomarket

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.SearchItem
import com.ibashkimi.cryptomarket.utils.toast


class SearchFragment : Fragment() {

    private lateinit var adapter: Adapter

    private var data: List<SearchItem>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            //title = getString(R.string.title_settings)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = Adapter()
        recyclerView.adapter = adapter

        val editTex = root.findViewById<EditText>(R.id.editText)
        editTex.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    adapter.updateData(emptyList())
                    return
                }
                val coins: List<SearchItem> = data ?: emptyList()
                val term = s.toString().trim().toLowerCase()
                adapter.updateData(coins.filter {
                    term in it.name.toLowerCase() || term in it.symbol.toLowerCase()
                })
            }
        })

        DataManager.loadSupportedCoins {
            data = when (it) {
                is ApiResponse.Success -> {
                    toast("Loaded: ${it.result.size}")
                    it.result
                }
                is ApiResponse.Failure -> {
                    toast("Failure")
                    emptyList()
                }
            }
        }

        return root
    }

    private fun onItemClicked(coin: SearchItem) {
        findNavController().navigate(HomeFragmentDirections.actionMainToCoin(coin.id, coin.name, coin.symbol))
    }

    inner class Adapter(private val coins: ArrayList<SearchItem> = ArrayList()) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false))
        }

        override fun getItemCount(): Int {
            return coins.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = coins[position]
            holder.name.text = item.name
            holder.symbol.text = item.symbol
            holder.itemView.setOnClickListener {
                onItemClicked(item)
            }
        }

        fun updateData(data: List<SearchItem>) {
            coins.clear()
            coins.addAll(data)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val symbol: TextView = itemView.findViewById(R.id.symbol)
    }
}
