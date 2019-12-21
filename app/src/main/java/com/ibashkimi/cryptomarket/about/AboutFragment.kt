package com.ibashkimi.cryptomarket.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = getString(R.string.title_about)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.aboutTitle.text = getString(R.string.app_name)
        binding.version.text = getString(R.string.version)
        binding.description.text = getString(R.string.description)
        binding.disclaimer.text = getString(R.string.disclaimer)

        return binding.root
    }

    class AboutFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.about)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            return when (preference.key) {
                "send_feedback" -> {
                    sendFeedback()
                    true
                }
                "licences" -> {
                    showLicenses()
                    true
                }
                else -> return super.onPreferenceTreeClick(preference)
            }
        }

        private fun showLicenses() {
            findNavController().navigate(AboutFragmentDirections.actionAboutToLicenses())
        }

        private fun sendFeedback() {
            val address = getString(R.string.author_email)
            val subject = getString(R.string.feedback_subject)

            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$address"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

            val chooserTitle = getString(R.string.feedback_chooser_title)
            startActivity(Intent.createChooser(emailIntent, chooserTitle))
        }
    }
}
