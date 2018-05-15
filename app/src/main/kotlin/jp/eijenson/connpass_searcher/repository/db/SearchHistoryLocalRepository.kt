package jp.eijenson.connpass_searcher.repository.db

import io.objectbox.Box
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn_
import jp.eijenson.connpass_searcher.repository.column.mapping.toSearchHistory
import jp.eijenson.connpass_searcher.repository.column.mapping.toSearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.mapping.toSearchHistoryList
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.model.SearchHistory
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/03/08.
 */
class SearchHistoryLocalRepository(private val searchHistoryTable: Box<SearchHistoryColumn>) {
    fun insert(searchHistory: SearchHistory): Long {
        return searchHistoryTable.put(searchHistory.toSearchHistoryColumn())
    }

    fun selectAll(): List<SearchHistory> = searchHistoryTable.all.toSearchHistoryList()

    fun selectSavedList(): List<SearchHistory> = searchHistoryTable
            .query()
            .equal(SearchHistoryColumn_.saveHistory,true)
            .build()
            .find()
            .toSearchHistoryList()

    fun selectId(request: RequestEvent): Long? {
        return searchHistoryTable.query()
                .equal(SearchHistoryColumn_.keyword, request.keyword ?: "")
                .build().findFirst()?.uniqueId
    }

    fun select(uniqueId: Long): SearchHistory? {
        return searchHistoryTable.get(uniqueId).toSearchHistory()
    }

    fun updateSaveHistory(uniqueId: Long) {
        val column = searchHistoryTable.get(uniqueId)
        column.saveHistory = true
        searchHistoryTable.put(column)
    }

    fun updateSearchDate(uniqueId: Long){
        val column = searchHistoryTable.get(uniqueId)
        column.searchDate = Date()
        searchHistoryTable.put(column)
    }

    fun delete(searchHistory: SearchHistory){
        val column = searchHistoryTable.find(SearchHistoryColumn_.keyword,searchHistory.keyword)[0]
        searchHistoryTable.remove(column)
    }

    fun deleteAll() {
        searchHistoryTable.removeAll()
    }
}