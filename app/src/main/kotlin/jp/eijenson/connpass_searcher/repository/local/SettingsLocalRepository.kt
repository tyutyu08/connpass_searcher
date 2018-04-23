package jp.eijenson.connpass_searcher.repository.local

import android.content.Context
import android.support.v7.preference.PreferenceManager

/**
 * Created by kobayashimakoto on 2018/04/23.
 */
class SettingsLocalRepository(private val context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val prefecture_key = "search_prefecture"
    var prefecture
        get() = preferences.getString(prefecture_key, "")
        set(value) = preferences.edit().putString(prefecture_key, value).apply()

}