package com.ibashkimi.cryptomarket.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import com.ibashkimi.cryptomarket.BaseActivity
import com.ibashkimi.cryptomarket.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        about_title.text = getString(R.string.app_name)
        version.text = getString(R.string.version)
        description.text = getString(R.string.description)
        disclaimer.text = getString(R.string.disclaimer)
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

            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + address))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

            val chooserTitle = getString(R.string.feedback_chooser_title)
            startActivity(Intent.createChooser(emailIntent, chooserTitle))
        }
    }
}
