package jp.eijenson.connpass_searcher.usecase

import io.reactivex.Observer
import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.presenter.Result
import jp.eijenson.connpass_searcher.repository.api.EventRepositoryImpl
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.repository.entity.mapping.toRequestEvent
import jp.eijenson.model.ResultEvent

/**
 * Created by kobayashimakoto on 2018/05/01.
 */
class SearchHistoryUseCase(searchHistoryLocalRepository: SearchHistoryLocalRepository) {
    private val searchUseCase = SearchUseCase(EventRepositoryImpl())
    private val searchHistoryList = searchHistoryLocalRepository.selectSavedList()


    fun checkNewArrival(subscribe: Observer<Result>) {
        searchHistoryList.forEach {
            searchUseCase.search(it.toRequestEvent(), object : DefaultObserver<ResultEvent>() {
                override fun onComplete() {
                    subscribe.onComplete()
                }

                override fun onNext(resultEvent: ResultEvent) {
                    val count = searchUseCase.countNewEvent(resultEvent, it.searchDate)

                    if (count > 0) {
                        subscribe.onNext(Result(resultEvent.resultsAvailable, it.keyword, count))
                    }
                }

                override fun onError(e: Throwable) {
                    subscribe.onError(e)
                }

            })

        }
    }
}