package jp.eijenson.connpass_searcher.repository.local

import android.content.Context

/**
 * Created by kobayashimakoto on 2018/04/17.
 */
class DevLocalRepository(context: Context) {
    val FILE_NAME = "dev"
    val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    val TEXT_KEY = "text"

    fun getText(): String {
        return preferences.getString(TEXT_KEY, "")
    }

    fun setText(text: String) {
        preferences
                .edit()
                .putString(TEXT_KEY, text)
                .apply()
    }
}