package jp.eijenson.connpass_searcher.repository.column.mapping

import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent

fun SearchHistoryColumn.toRequestEvent(): RequestEvent {
    return RequestEvent(
            eventId,
            keyword,
            keywordOr,
            ym,
            ymd,
            nickname,
            ownerNickname,
            seriesId,
            start,
            order,
            count,
            format
    )
}

fun List<SearchHistoryColumn>.toRequestEventList(): List<RequestEvent> {
    return map {
        it.toRequestEvent()
    }
}


fun createSearchHistoryColumn(requestEvent: RequestEvent): SearchHistoryColumn {
    return requestEvent.run {
        SearchHistoryColumn(
                0,
                eventId,
                keyword,
                keywordOr,
                ym,
                ymd,
                nickname,
                ownerNickname,
                seriesId,
                start,
                order,
                count,
                format,
                false
        )
    }
}