package com.ibashkimi.cryptomarket.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ibashkimi.cryptomarket.R

class AboutSectionsFragment : PreferenceFragmentCompat() {

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
