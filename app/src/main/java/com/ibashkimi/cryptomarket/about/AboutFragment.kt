package com.ibashkimi.cryptomarket.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ibashkimi.cryptomarket.R

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = getString(R.string.title_about)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        root.findViewById<TextView>(R.id.about_title).text = getString(R.string.app_name)
        root.findViewById<TextView>(R.id.version).text = getString(R.string.version)
        root.findViewById<TextView>(R.id.description).text = getString(R.string.description)
        root.findViewById<TextView>(R.id.disclaimer).text = getString(R.string.disclaimer)

        return root
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
                "licenses" -> {
                    showLicenses()
                    true
                }
                else -> return super.onPreferenceTreeClick(preference)
            }
        }

        private fun showLicenses() {
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
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
