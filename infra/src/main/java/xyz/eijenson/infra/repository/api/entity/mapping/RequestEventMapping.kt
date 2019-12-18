package xyz.eijenson.infra.repository.api.entity.mapping

import xyz.eijenson.infra.repository.api.entity.RequestEventJson
import jp.eijenson.model.RequestEvent
import jp.eijenson.model.SearchHistory
import java.util.Date

/**
 * Created by kobayashimakoto on 2018/04/23.
 */
fun RequestEventJson.toSearchHistory(): SearchHistory {
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

fun RequestEvent.toRequestEventJson(): RequestEventJson {
    return RequestEventJson(
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
        prefecture
    )
}

private fun convertColumn(int: Int): Int? = if (int != -1) int else null
private fun convertColumn(str: String): String? = if (str != "") str else null