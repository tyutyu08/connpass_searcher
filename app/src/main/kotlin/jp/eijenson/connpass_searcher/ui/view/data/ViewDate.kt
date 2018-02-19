package jp.eijenson.connpass_searcher.ui.view.data

import java.text.SimpleDateFormat
import java.util.*

class ViewDate(d: Date) {
    private val dateFormat = SimpleDateFormat("MM月dd日(E)", Locale.JAPAN)
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.JAPAN)
    val date = dateFormat.format(d)
    val time = timeFormat.format(d)
}