package jp.eijenson.connpass_searcher.domain.repository

import io.reactivex.Single
import jp.eijenson.model.RequestEvent
import jp.eijenson.model.ResultEvent
import jp.eijenson.model.SearchHistory
import java.util.Date

interface EventRemoteRepository {
    fun getAll(request: RequestEvent): Single<ResultEvent>
    fun getWhenAfter(request: RequestEvent, date: Date): Single<ResultEvent>
    fun getWhenAfter(request: SearchHistory, date: Date): Single<ResultEvent>}