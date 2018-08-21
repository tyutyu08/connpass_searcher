package jp.eijenson.connpass_searcher.view.ui.fragment

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import jp.eijenson.connpass_searcher.R


/**
 * Created by kobayashimakoto on 2018/04/19.
 */
class PrefsFragment : PreferenceFragmentCompat() {
    private lateinit var listener: Listener

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

        val enableNotification = this.findPreference("enable_notification") as CheckBoxPreference

        enableNotification.setOnPreferenceChangeListener { _, newValue ->
            val value = newValue as Boolean
            listener.onChangedNotification(value)
            true
        }


        val appVersion = this.findPreference("app_version")
        appVersion.title = appVersion.title.toString() + " " + versionName.toString()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw UnsupportedOperationException("Listenerを継承する必要があります")
        }
    }

    private val versionName by lazy {
        var pi: PackageInfo? = null
        try {
            pi = context?.packageManager?.getPackageInfo(context?.packageName,
                    PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        pi?.versionName
    }

    interface Listener {
        fun onChangedNotification(isEnable: Boolean)
    }
}