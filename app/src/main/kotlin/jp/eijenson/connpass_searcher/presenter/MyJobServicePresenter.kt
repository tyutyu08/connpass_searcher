package jp.eijenson.connpass_searcher.presenter

import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.ui.service.MyJobService
import jp.eijenson.connpass_searcher.usecase.SearchHistoryUseCase

/**
 * Created by makoto.kobayashi on 2018/04/24.
 */
class MyJobServicePresenter(private val service: MyJobService,
                            searchHistoryLocalRepository: SearchHistoryLocalRepository) {
    private val searchHistoryUseCase = SearchHistoryUseCase(searchHistoryLocalRepository)

    fun onStartJob() {
        searchHistoryUseCase.checkNewArrival(object : DefaultObserver<Result>() {
            override fun onComplete() {}

            override fun onNext(result: Result) {
                service.notification(result.keyword, result.count)
            }

            override fun onError(e: Throwable) {}

        })
    }
}

data class Result(
        val keyword: String,
        val count: Int
)
