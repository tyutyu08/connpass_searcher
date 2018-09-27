package jp.eijenson.connpass_searcher.domain.repository

import io.reactivex.Single
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.model.ResultEvent
import java.util.*

interface EventRemoteRepository {
    fun getAll(request: RequestEvent): Single<ResultEvent>
    fun getWhenAfter(request: RequestEvent, date: Date): Single<ResultEvent>
}