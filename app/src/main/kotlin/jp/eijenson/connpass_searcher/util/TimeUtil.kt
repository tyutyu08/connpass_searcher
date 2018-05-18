package jp.eijenson.connpass_searcher.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kobayashimakoto on 2018/05/02.
 */
fun nowString(): String {
    val df = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN)
    val date = Date()
    return df.format(date)
}

fun nowCalendar(): Calendar {
    return Calendar.getInstance(Locale.JAPAN)
}

fun isMidnight(nowHour: Int): Boolean {
    val startHour = 21..24
    val endHour = 0..6
    return nowHour in startHour || nowHour in endHour
}

fun Calendar.getHourOfDay(): Int {
    return this.get(Calendar.HOUR_OF_DAY)
}