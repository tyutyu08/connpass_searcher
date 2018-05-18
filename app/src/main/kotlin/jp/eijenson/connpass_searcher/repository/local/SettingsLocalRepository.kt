package jp.eijenson.connpass_searcher.repository.local

import android.content.Context
import android.support.v7.preference.PreferenceManager

/**
 * Created by kobayashimakoto on 2018/04/23.
 */
class SettingsLocalRepository( context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val prefecture_key = "search_prefecture"
    var prefecture: String
        get() = preferences.getString(prefecture_key, "")
        set(value) = preferences.edit().putString(prefecture_key, value).apply()

    private val KEY_ENABLE_NOTIFICATION = "enable_notification"
    var enableNotification
        get() = preferences.getBoolean(KEY_ENABLE_NOTIFICATION, true)
        set(value) = preferences.edit().putBoolean(KEY_ENABLE_NOTIFICATION, value).apply()

}