package jp.eijenson.connpass_searcher.domain.usecase

import io.reactivex.Observer
import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.view.presenter.Result
import jp.eijenson.connpass_searcher.infra.repository.api.EventApiRepository
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryBoxRepository
import jp.eijenson.connpass_searcher.infra.repository.api.entity.mapping.toRequestEvent
import jp.eijenson.model.ResultEvent

/**
 * Created by kobayashimakoto on 2018/05/01.
 */
class SearchHistoryUseCase(searchHistoryLocalRepository: SearchHistoryBoxRepository) {
    private val searchUseCase = SearchUseCase(EventApiRepository())
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