package xyz.eijenson.infra.repository.db

import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import xyz.eijenson.domain.repository.SearchHistoryLocalRepository
import xyz.eijenson.infra.repository.db.entity.SearchHistoryColumn
import xyz.eijenson.infra.repository.db.entity.mapping.toSearchHistory
import xyz.eijenson.infra.repository.db.entity.mapping.toSearchHistoryColumn
import xyz.eijenson.infra.repository.db.entity.mapping.toSearchHistoryList
import jp.eijenson.model.RequestEvent
import jp.eijenson.model.SearchHistory
import xyz.eijenson.infra.repository.db.entity.SearchHistoryColumn_
import java.util.Date

/**
 * Created by makoto.kobayashi on 2018/03/08.
 */
class SearchHistoryBoxRepository(
    boxStore: BoxStore
) : SearchHistoryLocalRepository {
    private val searchHistoryTable: Box<SearchHistoryColumn> = boxStore.boxFor()

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
        searchHistoryTable
            .query()
            .equal(SearchHistoryColumn_.keyword, searchHistory.keyword)
            .build()
            .remove()
    }

    override fun deleteAll() {
        searchHistoryTable.removeAll()
    }
}