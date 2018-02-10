package eijenson.connpass_searcher.repository

import eijenson.connpass_searcher.repository.entity.RequestEvent
import eijenson.model.ResultEvent
import io.reactivex.Observable

interface EventRepository {
    fun getEvent(request: RequestEvent): Observable<ResultEvent>
}