package com.ibashkimi.cryptomarket.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.cryptomarket.HomeFragmentDirections
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.coinlist.CoinAdapter
import com.ibashkimi.cryptomarket.model.Coin

class SearchFragment : Fragment() {

    private lateinit var adapter: CoinAdapter

    private var searchLiveData: LiveData<PagedList<Coin>>? = null

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = CoinAdapter(null) {
            requireActivity().findNavController(R.id.main_nav_host_fragment)
                .navigate(HomeFragmentDirections.actionMainToCoin(it.id))
        }
        recyclerView.adapter = adapter

        searchViewModel.searchChanged.observe(viewLifecycleOwner) {
            searchLiveData?.apply { removeObservers(viewLifecycleOwner) }
            searchLiveData = it
            it.observe(viewLifecycleOwner) { searchResult ->
                adapter.submitList(searchResult)
            }
        }

        root.findViewById<EditText>(R.id.editText).addTextChangedListener {
            searchViewModel.search(it.toString())
        }

        return root
    }
}
