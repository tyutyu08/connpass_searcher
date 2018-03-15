package jp.eijenson.connpass_searcher.repository.local

import io.objectbox.Box
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn_
import jp.eijenson.connpass_searcher.repository.column.mapping.createSearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.mapping.toSearchHistoryList
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.model.SearchHistory

/**
 * Created by makoto.kobayashi on 2018/03/08.
 */
class SearchHistoryLocalRepository(private val searchHistoryTable: Box<SearchHistoryColumn>) {
    // TODO:RequestEventは知らないほうがいい
    fun insert(request: RequestEvent): Long {
        return searchHistoryTable.put(createSearchHistoryColumn(request))
    }

    fun selectAll(): List<SearchHistory> = searchHistoryTable.all.toSearchHistoryList()

    // TODO:RequestEventは知らないほうがいい
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