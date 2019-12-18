package xyz.eijenson.infra.repository.local

import android.content.Context
import androidx.preference.PreferenceManager
import xyz.eijenson.domain.repository.SettingsLocalRepository

/**
 * Created by kobayashimakoto on 2018/04/23.
 */
class SettingsSharedRepository(context: Context) : SettingsLocalRepository {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val prefecture_key = "search_prefecture"
    override var prefecture: String
        get() = preferences.getString(prefecture_key, "") ?: ""
        set(value) = preferences.edit().putString(prefecture_key, value).apply()

    private val KEY_ENABLE_NOTIFICATION = "enable_notification"
    override var enableNotification
        get() = preferences.getBoolean(KEY_ENABLE_NOTIFICATION, true)
        set(value) = preferences.edit().putBoolean(KEY_ENABLE_NOTIFICATION, value).apply()
}