package com.ibashkimi.cryptomarket.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ibashkimi.cryptomarket.R
import com.ibashkimi.cryptomarket.applyNightMode


class PrefsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        onSharedPreferenceChanged(preferenceScreen.sharedPreferences, PreferenceHelper.KEY_CURRENCY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PreferenceLiveData(preferenceScreen.sharedPreferences, PreferenceHelper.KEY_CURRENCY) {
            PreferenceHelper.currencyName
        }.observe(viewLifecycleOwner, Observer {
            findPreference<Preference>(PreferenceHelper.KEY_CURRENCY)?.summary = it
        })
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            "currency" -> {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsToCurrencyPicker())
                true
            }
            else -> super.onPreferenceTreeClick(preference)
        }
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            PreferenceHelper.KEY_NIGHT_MODE -> {
                requireActivity().apply {
                    applyNightMode(PreferenceHelper.nightMode)
                }
            }
            /*PreferenceHelper.KEY_CURRENCY -> {
                findPreference<Preference>(key)?.summary = PreferenceHelper.currencyName
            }*/
        }
    }
}