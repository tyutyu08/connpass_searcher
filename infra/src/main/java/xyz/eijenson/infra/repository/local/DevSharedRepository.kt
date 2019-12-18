package xyz.eijenson.infra.repository.local

import android.content.Context
import xyz.eijenson.domain.repository.DevLocalRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by kobayashimakoto on 2018/04/17.
 */
class DevSharedRepository(context: Context) : DevLocalRepository {
    private val FILE_NAME = "dev"
    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    private val LOG_KEY = "log"

    override fun getLog(): String {
        val log = preferences.getString(LOG_KEY, "")
        return log ?: ""
    }

    override fun d(name: String, text: String) {
        val time = nowString()
        val value = "$time#$name: $text \n"
        setLog(value)
    }

    private fun nowString(): String {
        val df = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN)
        val date = Date()
        return df.format(date)
    }

    override fun setLog(text: String) {

        preferences
            .edit()
            .putString(LOG_KEY, text + getLog())
            .apply()
    }

    override fun clear() {
        preferences.edit().clear().apply()
    }
}