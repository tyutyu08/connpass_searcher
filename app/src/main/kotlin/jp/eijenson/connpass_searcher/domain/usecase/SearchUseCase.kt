package jp.eijenson.connpass_searcher.domain.usecase

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import jp.eijenson.connpass_searcher.domain.repository.EventRemoteRepository
import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.connpass_searcher.infra.repository.api.entity.mapping.toRequestEvent
import jp.eijenson.connpass_searcher.view.presenter.Result
import jp.eijenson.model.ResultEvent
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class SearchUseCase(private val eventRemoteRepository: EventRemoteRepository,
                    private val searchHistoryLocalRepository: SearchHistoryLocalRepository) {

    fun search(request: RequestEvent): Observable<ResultEvent> {
        return eventRemoteRepository.getAll(request)
                .retry(3)
    }

    fun checkNewArrival(): Observable<Result> {
        val list = searchHistoryLocalRepository.selectSavedList()
        return list.toObservable()
                .flatMapSingle { searchHistory ->
                    eventRemoteRepository.getAll(searchHistory.toRequestEvent())
                            .flatMap { it.events.toObservable() }
                            .filter { checkNewEvent(it.updatedAt, searchHistory.searchDate) }
                            .count()
                            .map { Result(searchHistory.uniqueId, searchHistory.keyword, it) }
                }
    }


    fun countNewEvent(resultEvent: ResultEvent, searchDate: Date): Int {
        var count = 0
        resultEvent.events.forEach { event ->
            if (checkNewEvent(event.updatedAt, searchDate)) {
                count++
            }
        }
        return count
    }

    fun checkNewEvent(newEvent: Date, searchDate: Date): Boolean = newEvent.after(searchDate)

}