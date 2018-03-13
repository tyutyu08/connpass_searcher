package jp.eijenson.connpass_searcher.repository.local

import io.objectbox.Box
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn_
import jp.eijenson.connpass_searcher.repository.column.mapping.createSearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.mapping.toRequestEventList
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent

/**
 * Created by makoto.kobayashi on 2018/03/08.
 */
class SearchHistoryLocalRepository(private val searchHistoryTable: Box<SearchHistoryColumn>) {
    fun insert(request: RequestEvent): Long {
        return searchHistoryTable.put(createSearchHistoryColumn(request))
    }

    fun selectAll(): List<RequestEvent> = searchHistoryTable.all.toRequestEventList()

    fun exists(request: RequestEvent): Boolean {
        return searchHistoryTable.query()
                .equal(SearchHistoryColumn_.keyword, request.keyword ?: "")
                .build()
                .count() != 0L
    }

    fun updateSaveHistory(uniqueId: Long) {
        val column = searchHistoryTable.get(uniqueId)
        column.saveHistory = true
        searchHistoryTable.put(column)
    }

    fun deleteAll() {
        searchHistoryTable.removeAll()
    }
}