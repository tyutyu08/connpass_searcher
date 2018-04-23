package jp.eijenson.connpass_searcher.ui.service

import android.app.job.JobParameters
import android.app.job.JobService
import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.presenter.NotificationPresenter
import jp.eijenson.connpass_searcher.repository.api.EventRepositoryImpl
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.repository.entity.mapping.toRequestEvent
import jp.eijenson.connpass_searcher.repository.local.DevLocalRepository
import jp.eijenson.connpass_searcher.usecase.SearchUseCase
import jp.eijenson.model.ResultEvent
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MyJobService : JobService() {
    override fun onStartJob(p0: JobParameters?): Boolean {
        val devRepository = DevLocalRepository(this.applicationContext)
        val text = devRepository.getText() + "Start : " + Date() + "\n"
        devRepository.setText(text)
        val table = (application as App).searchHistoryTable
        val searchHistoryList = SearchHistoryLocalRepository(table).selectSavedList()
        val searchUseCase = SearchUseCase(EventRepositoryImpl())
        searchHistoryList.forEach {
            searchUseCase.search(it.toRequestEvent(), object : DefaultObserver<ResultEvent>() {
                override fun onComplete() {
                }

                override fun onNext(resultEvent: ResultEvent) {
                    var count = 0
                    resultEvent.events.forEach { event ->
                        if(event.updatedAt.after(it.searchDate)){
                            count++
                        }
                    }
                    if(count > 0) {
                        NotificationPresenter(applicationContext).notifyNewArrival(it.keyword, count)
                    }
                }

                override fun onError(e: Throwable) {
                }

            })
        }

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        val devRepository = DevLocalRepository(this.applicationContext)
        val text = devRepository.getText() + "End : " + Date() + "\n"
        devRepository.setText(text)
        return true
    }
}