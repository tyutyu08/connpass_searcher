package jp.eijenson.connpass_searcher.repository

import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.model.ResultEvent
import io.reactivex.Observable

interface EventRepository {
    fun getEvent(request: RequestEvent): Observable<ResultEvent>
}