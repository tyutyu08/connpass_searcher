package jp.eijenson.connpass_searcher.infra.repository.db

import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.model.SearchHistory

/**
 * Created by makoto.kobayashi on 2018/03/08.
 */
class SearchHistoryDebugRepository() : SearchHistoryLocalRepository {
    override fun insert(searchHistory: SearchHistory): Long {
        return 0
    }

    override fun selectSavedList(): List<SearchHistory> {
        return emptyList()
    }

    override fun selectId(request: RequestEvent): Long? {
        return null
    }

    override fun select(uniqueId: Long): SearchHistory? {
        return null
    }

    override fun updateSaveHistory(uniqueId: Long) {}

    override fun updateSearchDate(uniqueId: Long) {}

    override fun delete(searchHistory: SearchHistory) {}

    override fun deleteAll() {}

}