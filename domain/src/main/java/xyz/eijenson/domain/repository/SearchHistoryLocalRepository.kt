package jp.eijenson.connpass_searcher.domain.repository

import jp.eijenson.model.RequestEvent
import jp.eijenson.model.SearchHistory

interface SearchHistoryLocalRepository {
    fun insert(searchHistory: SearchHistory): Long
    fun selectSavedList(): List<SearchHistory>
    fun selectId(request: RequestEvent): Long?
    fun select(uniqueId: Long): SearchHistory?
    fun updateSaveHistory(uniqueId: Long)
    fun updateSearchDate(uniqueId: Long)
    fun delete(searchHistory: SearchHistory)
    fun deleteAll()
}