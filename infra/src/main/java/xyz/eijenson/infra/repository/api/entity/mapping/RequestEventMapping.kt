package xyz.eijenson.infra.repository.api.entity.mapping

import jp.eijenson.model.RequestEvent
import jp.eijenson.model.SearchHistory
import xyz.eijenson.infra.repository.api.entity.RequestEventJson
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

private fun convertColumn(int: Int?): Int? = if (int != null && int != -1) int else null
private fun convertColumn(str: String?): String? = if (str != null && str != "") str else null