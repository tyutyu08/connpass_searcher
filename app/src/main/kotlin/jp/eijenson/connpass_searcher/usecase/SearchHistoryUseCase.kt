package jp.eijenson.connpass_searcher.usecase

import io.reactivex.Observer
import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.presenter.Result
import jp.eijenson.connpass_searcher.infra.repository.api.EventRepositoryImpl
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.infra.entity.mapping.toRequestEvent
import jp.eijenson.model.ResultEvent

/**
 * Created by kobayashimakoto on 2018/05/01.
 */
class SearchHistoryUseCase(searchHistoryLocalRepository: SearchHistoryLocalRepository) {
    private val searchUseCase = SearchUseCase(EventRepositoryImpl())
    private val searchHistoryRepository = searchHistoryLocalRepository


    fun checkNewArrival(subscribe: Observer<Result>) {
        val list = searchHistoryRepository.selectSavedList()
        list.forEach {
            searchUseCase.search(it.toRequestEvent(), object : DefaultObserver<ResultEvent>() {
                override fun onComplete() {
                    subscribe.onComplete()
                }

                override fun onNext(resultEvent: ResultEvent) {
                    val count = searchUseCase.countNewEvent(resultEvent, it.searchDate)

                    if (count > 0) {
                        subscribe.onNext(Result(it.uniqueId.toInt(), it.keyword, count))
                    }
                }

                override fun onError(e: Throwable) {
                    subscribe.onError(e)
                }

            })

        }
    }
}