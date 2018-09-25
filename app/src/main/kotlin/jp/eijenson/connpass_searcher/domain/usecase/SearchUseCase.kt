package jp.eijenson.connpass_searcher.domain.usecase

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.rxkotlin.toObservable
import jp.eijenson.connpass_searcher.domain.repository.EventRemoteRepository
import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.connpass_searcher.infra.repository.api.entity.mapping.toRequestEvent
import jp.eijenson.model.ResultEvent

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class SearchUseCase(private val eventRemoteRepository: EventRemoteRepository,
                    private val searchHistoryLocalRepository: SearchHistoryLocalRepository) {

    /**
     * 検索する
     */
    fun search(request: RequestEvent): Single<ResultEvent> {
        return eventRemoteRepository.getAll(request)
                .retry(3)
    }

    /**
     * 保存条件で検索して、新しいイベントがあるかチェックする
     */
    fun checkNewArrival(): Observable<Result> {
        val list = searchHistoryLocalRepository.selectSavedList()
        return list.toObservable()
                .flatMapSingle { searchHistory ->
                    search(searchHistory.toRequestEvent())
                            .flatMapPublisher { it.events.toFlowable() }
                            .filter { it.updatedAt.after(searchHistory.searchDate) }
                            .count()
                            .map { Result(searchHistory.uniqueId, searchHistory.keyword, it) }
                }
    }
}

data class Result(
        val id: Long,
        val keyword: String,
        val count: Long
)
