package jp.eijenson.connpass_searcher.presenter

import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.repository.api.EventRepositoryImpl
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.repository.entity.mapping.toRequestEvent
import jp.eijenson.connpass_searcher.repository.local.DevLocalRepository
import jp.eijenson.connpass_searcher.ui.service.MyJobService
import jp.eijenson.connpass_searcher.usecase.SearchUseCase
import jp.eijenson.model.ResultEvent
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/04/24.
 */
class MyJobServicePresenter(private val service: MyJobService,
                            private val devLocalRepository: DevLocalRepository,
                            searchHistoryLocalRepository: SearchHistoryLocalRepository) {
    private val searchUseCase = SearchUseCase(EventRepositoryImpl())
    private val searchHistoryList = searchHistoryLocalRepository.selectSavedList()

    fun onStartJob() {
        val text = devLocalRepository.getText() + "Start : " + Date() + "\n"
        devLocalRepository.setText(text)
        searchHistoryList.forEach {
            searchUseCase.search(it.toRequestEvent(), object : DefaultObserver<ResultEvent>() {
                override fun onComplete() {}

                override fun onNext(resultEvent: ResultEvent) {
                    val count = searchUseCase.countNewEvent(resultEvent, it.searchDate)

                    if (count > 0) {
                        service.notification(it.keyword, count)
                    }
                }

                override fun onError(e: Throwable) {}

            })
        }
    }

    fun onStopJob() {
        val text = devLocalRepository.getText() + "End : " + Date() + "\n"
        devLocalRepository.setText(text)
    }
}