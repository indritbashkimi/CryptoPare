package com.ibashkimi.cryptomarket.coinlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.model.Coin
import java.text.DecimalFormatSymbols


class CoinAdapter(private val imageLoader: ImageLoader?, private val clickListener: OnCoinClickedListener)
    : PagedListAdapter<Coin, CoinViewHolder>(coinDiffCallback) {

    val decimalFormatSymbols = DecimalFormatSymbols()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder = CoinViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_crypto_5, parent, false), clickListener)

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin: Coin? = getItem(position)
        if (coin == null)
            holder.clear()
        else
            holder.bind(coin, imageLoader)
    }

    companion object {
        val coinDiffCallback = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem.rank == newItem.rank
            }

            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface ImageLoader {
    fun loadImage(coin: Coin, imageView: ImageView)
}

interface OnCoinClickedListener {
    fun onCoinClicked(coin: Coin)
}
