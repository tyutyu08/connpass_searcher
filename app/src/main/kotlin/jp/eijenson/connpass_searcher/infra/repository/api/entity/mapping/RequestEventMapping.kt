package jp.eijenson.connpass_searcher.infra.repository.api.entity.mapping

import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.model.SearchHistory
import java.util.*

/**
 * Created by kobayashimakoto on 2018/04/23.
 */
fun RequestEvent.toSearchHistory(): SearchHistory {
    return SearchHistory(
            0,
            eventId ?: -1,
            keyword ?: "",
            keywordOr ?: "",
            ym ?: -1,
            ymd ?: -1,
            nickname ?: "",
            ownerNickname ?: "",
            seriesId ?: -1,
            start ?: -1,
            order ?: -1,
            count ?: -1,
            format ?: "",
            false,
            Date(),
            prefecture
    )
}

fun SearchHistory.toRequestEvent(): RequestEvent {
    return RequestEvent(
            convertColumn(eventId),
            convertColumn(keyword),
            convertColumn(keywordOr),
            convertColumn(ym),
            convertColumn(ymd),
            convertColumn(nickname),
            convertColumn(ownerNickname),
            convertColumn(seriesId),
            convertColumn(start),
            convertColumn(order),
            convertColumn(count),
            convertColumn(format),
            prefecture
    )
}

private fun convertColumn(int: Int): Int? = if (int != -1) int else null
private fun convertColumn(str: String): String? = if (str != "") str else null