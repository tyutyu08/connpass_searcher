package jp.eijenson.connpass_searcher.infra.repository.local

import android.content.Context
import jp.eijenson.connpass_searcher.util.nowString

/**
 * Created by kobayashimakoto on 2018/04/17.
 */
class DevLocalRepository(context: Context) {
    private val FILE_NAME = "dev"
    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    private val LOG_KEY = "log"

    fun getLog(): String {
        val log = preferences.getString(LOG_KEY, "")
        return log ?: ""
    }

    fun d(name: String, text: String) {
        val time = nowString()
        val value = "$time#$name: $text \n"
        setLog(value)
    }

    private fun setLog(text: String) {

        preferences
                .edit()
                .putString(LOG_KEY, text + getLog())
                .apply()
    }

    fun clear() {
        preferences.edit().clear().apply()
    }
}