package jp.eijenson.connpass_searcher.ui.view.data

import jp.eijenson.connpass_searcher.repository.column.EventTable_.waiting
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
        var isFavorite: Boolean
) {
    fun viewAccept(): String {
        return if (isAccept()) "参加可能" else """${waiting}人キャンセル待ち"""
    }

    fun isAccept(): Boolean {
        return accepted <= limit
    }
}