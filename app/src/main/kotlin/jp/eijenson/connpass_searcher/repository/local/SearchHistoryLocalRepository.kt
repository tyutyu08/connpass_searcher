package jp.eijenson.connpass_searcher.repository.local

import io.objectbox.Box
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.mapping.createSearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.mapping.toRequestEventList
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent

/**
 * Created by makoto.kobayashi on 2018/03/08.
 */
class SearchHistoryLocalRepository(private val searchHistoryTable: Box<SearchHistoryColumn>) {
    fun insert(request: RequestEvent) {
        searchHistoryTable.put(createSearchHistoryColumn(request))
    }

    fun selectAll(): List<RequestEvent> = searchHistoryTable.all.toRequestEventList()
}