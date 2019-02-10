package jp.eijenson.connpass_searcher.view.data

import jp.eijenson.model.Prefecture
import jp.eijenson.model.Series
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/03/06.
 */
class ViewEvent(
        val eventId: Long,
        val title: String,
        val eventUrl: String,
        val startedAt: Date,
        val endedAt: Date,
        val accepted: Int,
        val limit: Int,
        val series: Series,
        val prefecture: Prefecture,
        val address:String,
        val waiting: Int,
        var isFavorite: Boolean
) {
    fun viewAccept(): String {
        return if (isAccept()) "参加可能" else "要確認"
    }

    fun isAccept(): Boolean {
        return accepted <= limit
    }
}