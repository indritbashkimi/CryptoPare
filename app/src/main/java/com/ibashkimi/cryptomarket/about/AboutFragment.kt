package com.ibashkimi.cryptomarket.about

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ibashkimi.cryptomarket.BuildConfig
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = getString(R.string.title_about)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.aboutTitle.text = getString(R.string.app_name)
        binding.version.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        binding.description.apply {
            text = getString(R.string.description)
            setOnClickListener {
                openUrl("https://docs.coincap.io/")
            }
        }
        binding.disclaimer.text = getString(R.string.disclaimer)
        binding.sourceCode.setOnClickListener {
            openUrl(requireContext().getString(R.string.source_code_url))
        }

        return binding.root
    }

    private fun openUrl(url: String) {
        CustomTabsIntent.Builder().build().launchUrl(
            requireContext(),
            Uri.parse(url)
        )
    }
}
