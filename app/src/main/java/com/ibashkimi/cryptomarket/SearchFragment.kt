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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.cryptomarket.livedata.SearchViewModel
import com.ibashkimi.cryptomarket.model.Coin


class SearchFragment : Fragment(), OnCoinClickedListener {

    private lateinit var adapter: CoinAdapter

    private var searchLiveData: LiveData<PagedList<Coin>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        /*root.findViewById<Toolbar>(R.id.toolbar).apply {
            //title = getString(R.string.title_settings)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }*/

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        //val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        //recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = CoinAdapter(null, this)
        recyclerView.adapter = adapter

        val searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        searchViewModel.searchChanged.observe(viewLifecycleOwner, Observer {
            searchLiveData?.apply { removeObservers(viewLifecycleOwner) }
            searchLiveData = it
            it.observe(viewLifecycleOwner, Observer { searchResult ->
                adapter.submitList(searchResult)
            })
        })

        val editTex = root.findViewById<EditText>(R.id.editText)
        editTex.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchViewModel.search(s.toString())
            }
        })

        return root
    }

    override fun onCoinClicked(coin: Coin) {
        requireActivity().findNavController(R.id.main_nav_host_fragment)
                .navigate(HomeFragmentDirections
                        .actionMainToCoin(coin.id, coin.name, coin.symbol))
    }
}
