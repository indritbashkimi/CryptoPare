package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ibashkimi.cryptomarket.BaseActivity
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.utils.toast

class SettingsFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_settings, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = getString(R.string.title_settings)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        PreferenceHelper.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        PreferenceHelper.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (PreferenceHelper.KEY_NIGHT_MODE == key) {
            val mainActivity = requireActivity() as BaseActivity?
            mainActivity?.apply {
                applyNightMode(PreferenceHelper.nightMode)
                recreate()
            } ?.apply {
                toast("Cannot change theme")
            }
        }
    }
}
