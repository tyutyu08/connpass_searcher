package jp.eijenson.connpass_searcher.presenter

import com.crashlytics.android.Crashlytics
import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.content.JobServiceContent
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.usecase.SearchHistoryUseCase
import jp.eijenson.connpass_searcher.util.getHourOfDay
import jp.eijenson.connpass_searcher.util.isMidnight
import jp.eijenson.connpass_searcher.util.nowCalendar

/**
 * Created by makoto.kobayashi on 2018/04/24.
 */
class MyJobServicePresenter(private val service: JobServiceContent,
                            searchHistoryLocalRepository: SearchHistoryLocalRepository) {
    private val searchHistoryUseCase = SearchHistoryUseCase(searchHistoryLocalRepository)

    fun onStartJob() {
        // 夜中なら実行しない
        if (isMidnight(nowCalendar().getHourOfDay())) {
            return
        }

        searchHistoryUseCase.checkNewArrival(object : DefaultObserver<Result>() {
            override fun onComplete() {}

            override fun onNext(result: Result) {
                service.showNotification(result.keyword, result.count)
            }

            override fun onError(e: Throwable) {
                Crashlytics.logException(e)
            }

        })
    }
}

data class Result(
        val keyword: String,
        val count: Int
)
