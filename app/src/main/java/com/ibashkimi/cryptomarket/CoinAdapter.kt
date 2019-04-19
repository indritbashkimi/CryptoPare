package com.ibashkimi.cryptomarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.utils.CurrencySymbolResolver
import com.ibashkimi.cryptomarket.utils.priceFormat
import java.text.DecimalFormatSymbols


class CoinAdapter(private val imageLoader: ImageLoader?, private val clickListener: OnCoinClicked)
    : PagedListAdapter<Coin, CoinAdapter.CryptoViewHolder>(coinDiffCallback) {

    val decimalFormatSymbols = DecimalFormatSymbols()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder = CryptoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_crypto_5, parent, false))

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val coin: Coin? = getItem(position)
        if (coin == null)
            holder.clear()
        else
            holder.bind(coin, imageLoader)
    }

    inner class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var rank: TextView = itemView.findViewById(R.id.rank)
        private var icon: ImageView? = itemView.findViewById(R.id.icon)
        private var name: TextView = itemView.findViewById(R.id.name)
        private var symbol: TextView = itemView.findViewById(R.id.symbol)
        private var price: TextView = itemView.findViewById(R.id.price)
        private var oneHourChange: TextView? = itemView.findViewById(R.id.change1h)
        private var twentyFourHourChange: TextView? = itemView.findViewById(R.id.change24h)
        private var sevenDayChange: TextView? = itemView.findViewById(R.id.change7d)

        fun clear() {
            rank.text = null
            name.text = null
            symbol.text = null
            price.text = null
            oneHourChange?.text = null
            twentyFourHourChange?.text = null
            sevenDayChange?.text = null
            icon?.setImageDrawable(null)
        }

        fun bind(coin: Coin, imageLoader: ImageLoader?) {
            rank.text = itemView.context.getString(R.string.rank_value, coin.rank)
            name.text = coin.name
            symbol.text = coin.symbol
            price.text = price.context
                    .getString(R.string.price, CurrencySymbolResolver.resolve(price.context, coin.currency),
                            coin.price?.priceFormat(decimalFormatSymbols))
            oneHourChange?.apply {
                text = context.getString(R.string.percent_change, coin.percentChange1h)
            }
            twentyFourHourChange?.apply {
                text = context.getString(R.string.percent_change, coin.percentChange24h)
            }

            sevenDayChange?.apply {
                text = context.getString(R.string.percent_change, coin.percentChange7d)
            }

            icon?.let { imageLoader?.loadImage(coin, it) }

            val positiveColor = ContextCompat.getColor(itemView.context, R.color.positive_color)
            val negativeColor = ContextCompat.getColor(itemView.context, R.color.negative_color)

            if (coin.percentChange1h == null) {
                oneHourChange?.text = "?"
            } else {
                oneHourChange?.setTextColor(when {
                    coin.percentChange1h.contains("-") -> negativeColor
                    else -> positiveColor
                })
            }

            if (coin.percentChange24h == null)
                twentyFourHourChange?.text = "?"
            else
                twentyFourHourChange?.setTextColor(when {
                    coin.percentChange24h.contains("-") -> negativeColor
                    else -> positiveColor
                })

            if (coin.percentChange7d == null)
                sevenDayChange?.text = "?"
            else
                sevenDayChange?.setTextColor(when {
                    coin.percentChange7d.contains("-") -> negativeColor
                    else -> positiveColor
                })
            itemView.setOnClickListener {
                clickListener.onCoinClicked(coin)
            }
        }
    }

    interface ImageLoader {
        fun loadImage(coin: Coin, imageView: ImageView)
    }

    interface OnCoinClicked {
        fun onCoinClicked(coin: Coin)
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


