package com.ibashkimi.cryptomarket

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.cryptomarket.model.Coin
import com.ibashkimi.cryptomarket.utils.CurrencySymbolResolver
import java.text.DecimalFormat


class CoinViewHolder(itemView: View, val clickListener: OnCoinClickedListener) : RecyclerView.ViewHolder(itemView) {
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
        val priceFormatter = DecimalFormat(if (coin.price!!.toDouble() < 1) "#.########" else ".##")
        price.text = price.context
                .getString(R.string.price, CurrencySymbolResolver.resolve(price.context, coin.currency),
                        priceFormatter.format(coin.price.toDouble()))
        val changeFormatter = DecimalFormat("#.##")
        oneHourChange?.apply {
            text = context.getString(R.string.percent_change, coin.percentChange1h)
        }
        twentyFourHourChange?.apply {
            val t: String = coin.percentChange24h?.toDoubleOrNull()?.run { changeFormatter.format(this) } ?: "-"
            text = context.getString(R.string.percent_change, t)
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