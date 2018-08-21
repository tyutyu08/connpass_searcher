package jp.eijenson.connpass_searcher.infra.repository.db

import io.objectbox.Box
import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.infra.entity.RequestEvent
import jp.eijenson.connpass_searcher.infra.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.infra.repository.column.SearchHistoryColumn_
import jp.eijenson.connpass_searcher.infra.repository.column.mapping.toSearchHistory
import jp.eijenson.connpass_searcher.infra.repository.column.mapping.toSearchHistoryColumn
import jp.eijenson.connpass_searcher.infra.repository.column.mapping.toSearchHistoryList
import jp.eijenson.model.SearchHistory
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/03/08.
 */
class SearchHistoryBoxRepository(private val searchHistoryTable: Box<SearchHistoryColumn>) : SearchHistoryLocalRepository {
    override fun insert(searchHistory: SearchHistory): Long {
        return searchHistoryTable.put(searchHistory.toSearchHistoryColumn())
    }

    override fun selectSavedList(): List<SearchHistory> = searchHistoryTable
            .query()
            .equal(SearchHistoryColumn_.saveHistory, true)
            .build()
            .find()
            .toSearchHistoryList()

    override fun selectId(request: RequestEvent): Long? {
        return searchHistoryTable.query()
                .equal(SearchHistoryColumn_.keyword, request.keyword ?: "")
                .build().findFirst()?.uniqueId
    }

    override fun select(uniqueId: Long): SearchHistory? {
        return searchHistoryTable.get(uniqueId).toSearchHistory()
    }

    override fun updateSaveHistory(uniqueId: Long) {
        val column = searchHistoryTable.get(uniqueId)
        column.saveHistory = true
        searchHistoryTable.put(column)
    }

    override fun updateSearchDate(uniqueId: Long) {
        val column = searchHistoryTable.get(uniqueId)
        column.searchDate = Date()
        searchHistoryTable.put(column)
    }

    override fun delete(searchHistory: SearchHistory) {
        val column = searchHistoryTable.find(SearchHistoryColumn_.keyword, searchHistory.keyword)[0]
        searchHistoryTable.remove(column)
    }

    override fun deleteAll() {
        searchHistoryTable.removeAll()
    }
}