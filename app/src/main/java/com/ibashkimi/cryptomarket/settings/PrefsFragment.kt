package com.ibashkimi.cryptomarket.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.ibashkimi.cryptomarket.R


class PrefsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}