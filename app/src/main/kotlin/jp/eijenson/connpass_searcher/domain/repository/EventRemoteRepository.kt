package jp.eijenson.connpass_searcher.domain.repository

import io.reactivex.Observable
import jp.eijenson.connpass_searcher.infra.entity.RequestEvent
import jp.eijenson.model.ResultEvent

interface EventRemoteRepository {
    fun getAll(request: RequestEvent): Observable<ResultEvent>
}