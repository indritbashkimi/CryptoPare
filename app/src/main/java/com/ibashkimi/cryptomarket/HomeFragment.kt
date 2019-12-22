package com.ibashkimi.cryptomarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val navController =
            Navigation.findNavController(root.findViewById(R.id.home_nav_host_fragment))
        root.findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            setupWithNavController(navController)
        }
        return root
    }
}