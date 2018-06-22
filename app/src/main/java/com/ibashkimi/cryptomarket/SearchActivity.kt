package com.ibashkimi.cryptomarket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ibashkimi.cryptomarket.data.ApiResponse
import com.ibashkimi.cryptomarket.data.DataManager
import com.ibashkimi.cryptomarket.model.Coin


class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: Adapter

    private var data: List<Coin>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = Adapter()
        recyclerView.adapter = adapter

        val editTex = findViewById<EditText>(R.id.editText)
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
                val coins: List<Coin> = data ?: emptyList()
                val term = s.toString().trim().toLowerCase()
                adapter.updateData(coins.filter {
                    term in it.name.toLowerCase() || term in it.symbol.toLowerCase()
                }.sortedBy { it.rank })
            }
        })

        DataManager.loadSupportedCoins {
            data = when (it) {
                is ApiResponse.Success -> {
                    it.result
                }
                is ApiResponse.Failure -> {
                    Toast.makeText(this@SearchActivity, "Failure", Toast.LENGTH_SHORT).show()
                    emptyList()
                }
            }
        }
    }

    private fun onItemClicked(coin: Coin) {
        val intent = Intent(this, CoinActivity::class.java)
        intent.action = coin.id
        intent.putExtra("name", coin.name)
        intent.putExtra("symbol", coin.symbol)
        startActivity(intent)
    }

    inner class Adapter(private val coins: ArrayList<Coin> = ArrayList()) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

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
            holder.itemView.setOnClickListener({
                onItemClicked(item)
            })
        }

        fun updateData(data: List<Coin>) {
            coins.clear()
            coins.addAll(data)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val symbol: TextView = itemView.findViewById(R.id.symbol)
    }
}
