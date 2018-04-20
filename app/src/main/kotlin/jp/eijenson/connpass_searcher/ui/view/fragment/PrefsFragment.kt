package jp.eijenson.connpass_searcher.ui.view.fragment

import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import jp.eijenson.connpass_searcher.R

/**
 * Created by kobayashimakoto on 2018/04/19.
 */
class PrefsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val prefPrefecture = this.findPreference("search_prefecture") as ListPreference

        val entry: CharSequence? = prefPrefecture.entry
        prefPrefecture.summary = entry
        prefPrefecture.setOnPreferenceChangeListener { preference, newValue ->
            val index = prefPrefecture.findIndexOfValue((newValue as String))
            val key = prefPrefecture.entries[index]
            preference.summary = key
            true
        }
    }
}