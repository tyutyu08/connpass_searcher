package jp.eijenson.connpass_searcher.repository

import io.reactivex.Observable
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.model.ResultEvent

interface EventRepository {
    fun getAll(request: RequestEvent): Observable<ResultEvent>
}