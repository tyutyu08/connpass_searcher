package jp.eijenson.connpass_searcher.repository.column.mapping

import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.model.SearchHistory
import java.util.*

fun SearchHistoryColumn.toSearchHistory(): SearchHistory {
    return SearchHistory(
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
            saveHistory,
            searchDate,
            prefecture

    )
}

fun List<SearchHistoryColumn>.toSearchHistoryList(): List<SearchHistory> {
    return map {
        it.toSearchHistory()
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
                false,
                Date(),
                prefecture
        )
    }
}