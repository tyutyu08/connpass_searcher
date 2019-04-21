package jp.eijenson.connpass_searcher.infra.repository.db.entity.mapping

import jp.eijenson.connpass_searcher.infra.repository.db.entity.SearchHistoryColumn
import jp.eijenson.model.SearchHistory

fun SearchHistoryColumn.toSearchHistory(): SearchHistory {
    return SearchHistory(
        uniqueId ?: 0,
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

fun SearchHistory.toSearchHistoryColumn(): SearchHistoryColumn {
    return SearchHistoryColumn(
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
        searchDate,
        prefecture
    )
}