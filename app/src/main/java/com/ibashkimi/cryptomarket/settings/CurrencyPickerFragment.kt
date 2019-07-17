package com.ibashkimi.cryptomarket.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.data.api.coincap.model.RateItem

class CurrencyPickerFragment : Fragment() {

    private val viewModel: CurrencyPickerViewModel by viewModels()

    private lateinit var selectedCurrency: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_currency_picker, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.select_currency)
            setNavigationIcon(R.drawable.ic_nav_cancel)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        val currencyAdapter = CurrencyAdapter()
        root.findViewById<RecyclerView>(R.id.recyclerView).apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            adapter = currencyAdapter
        }

        val errorView = root.findViewById<View>(R.id.error)
        val progress = root.findViewById<ProgressBar>(R.id.progress)

        selectedCurrency = PreferenceHelper.currency

        viewModel.currencies.observe(viewLifecycleOwner, Observer {
            currencyAdapter.data = it ?: emptyList()
            progress.isVisible = false
            currencyAdapter.notifyDataSetChanged()
            if (it == null) {
                errorView.isVisible = true
                //toast("Cannot load currencies. Try again.")
            }
        })

        return root
    }

    inner class CurrencyAdapter : RecyclerView.Adapter<CurrencyViewHolder>() {

        private val currencies = ArrayList<RateItem>()

        var data: List<RateItem>
            get() = currencies
            set(value) {
                currencies.clear()
                currencies.addAll(value)
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
            return CurrencyViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
            val item = data[position]
            if (item.currencySymbol != null) {
                holder.currencySymbol.text = item.currencySymbol
            } else {
                holder.currencySymbol.isVisible = false
            }
            holder.symbol.text = item.symbol
            holder.selected.isVisible = item.id == selectedCurrency
            holder.itemView.setOnClickListener {
                selectedCurrency = item.id
                PreferenceHelper.currency = item.id
                PreferenceHelper.currencySymbol = item.currencySymbol
                PreferenceHelper.currencyName = item.symbol
                notifyDataSetChanged()
            }
        }

        override fun onViewRecycled(holder: CurrencyViewHolder) {
            super.onViewRecycled(holder)
            holder.currencySymbol.text = null
            holder.currencySymbol.isVisible = true
            holder.symbol.text = null
            holder.selected.isVisible = false
        }
    }

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencySymbol: TextView = itemView.findViewById(R.id.currencySymbol)
        val symbol: TextView = itemView.findViewById(R.id.symbol)
        val selected: View = itemView.findViewById(R.id.selected)
    }
}