package jp.eijenson.connpass_searcher.view.presenter

import com.crashlytics.android.Crashlytics
import io.reactivex.rxkotlin.subscribeBy
import jp.eijenson.connpass_searcher.domain.usecase.SearchUseCase
import jp.eijenson.connpass_searcher.util.getHourOfDay
import jp.eijenson.connpass_searcher.util.isMidnight
import jp.eijenson.connpass_searcher.util.nowCalendar
import jp.eijenson.connpass_searcher.view.content.JobServiceContent

/**
 * Created by makoto.kobayashi on 2018/04/24.
 */
class MyJobServicePresenter(
    private val service: JobServiceContent.View,
    private val searchUseCase: SearchUseCase
) : JobServiceContent.Presenter {

    override fun onStartJob() {
        // 夜中なら実行しない
        if (isMidnight(nowCalendar().getHourOfDay())) {
            return
        }

        searchUseCase.checkNewArrival()
            .subscribeBy(
                onError = { e ->
                    Crashlytics.logException(e)
                    service.log("onError")
                },
                onNext = { result ->
                    service.showNotification(result.id.toInt(), result.keyword, result.count.toInt())

                }
            )
    }
}

