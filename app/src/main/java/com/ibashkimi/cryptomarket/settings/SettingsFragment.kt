package com.ibashkimi.cryptomarket.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.ibashkimi.cryptomarket.R

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_settings, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = getString(R.string.title_settings)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        root.findViewById<AppBarLayout>(R.id.appBar).apply {
            isLiftOnScroll = true
        }

        return root
    }
}
