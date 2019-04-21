package jp.eijenson.connpass_searcher.view.ui.fragment

import android.app.job.JobScheduler
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.view.content.SettingsContent
import jp.eijenson.connpass_searcher.view.ui.service.FirstRunJobService

/**
 * Created by kobayashimakoto on 2018/04/19.
 */
class PrefsFragment : PreferenceFragmentCompat(), SettingsContent.View {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<ListPreference>("search_prefecture")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                val index = findIndexOfValue((newValue as String))
                val key = entries[index]
                preference.summary = key
                true
            }
        }

        findPreference<CheckBoxPreference>("enable_notification")?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                val value = newValue as Boolean
                onChangedNotification(value)
                true
            }
        }



        findPreference<Preference>("app_version")?.apply {
            title = title.toString() + " " + versionName.toString()
        }
    }

    private val versionName by lazy {
        var pi: PackageInfo? = null
        try {
            pi = context?.packageManager?.getPackageInfo(
                context?.packageName,
                PackageManager.GET_META_DATA
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        pi?.versionName
    }

    fun onChangedNotification(isEnable: Boolean) {
        val context = context ?: return
        if (isEnable) {
            FirstRunJobService.schedule(context)
        } else {
            val scheduler = activity?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancelAll()
        }
    }
}