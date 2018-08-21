package jp.eijenson.connpass_searcher.view.presenter

import com.crashlytics.android.Crashlytics
import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.view.content.JobServiceContent
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryBoxRepository
import jp.eijenson.connpass_searcher.domain.usecase.SearchHistoryUseCase
import jp.eijenson.connpass_searcher.util.getHourOfDay
import jp.eijenson.connpass_searcher.util.isMidnight
import jp.eijenson.connpass_searcher.util.nowCalendar

/**
 * Created by makoto.kobayashi on 2018/04/24.
 */
class MyJobServicePresenter(private val service: JobServiceContent,
                            searchHistoryLocalRepository: SearchHistoryBoxRepository) {
    private val searchHistoryUseCase = SearchHistoryUseCase(searchHistoryLocalRepository)

    fun onStartJob() {
        // 夜中なら実行しない
        if (isMidnight(nowCalendar().getHourOfDay())) {
            return
        }

        searchHistoryUseCase.checkNewArrival(object : DefaultObserver<Result>() {
            override fun onComplete() {}

            override fun onNext(result: Result) {
                service.showNotification(result.id, result.keyword, result.count)
            }

            override fun onError(e: Throwable) {
                Crashlytics.logException(e)
                service.log("onError")
            }

        })
    }
}

data class Result(
        val id: Int,
        val keyword: String,
        val count: Int
)
