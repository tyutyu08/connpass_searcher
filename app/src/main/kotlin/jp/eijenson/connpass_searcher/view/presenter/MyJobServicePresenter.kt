package jp.eijenson.connpass_searcher.view.presenter

import com.crashlytics.android.Crashlytics
import io.reactivex.rxkotlin.subscribeBy
import jp.eijenson.connpass_searcher.domain.usecase.SearchUseCase
import jp.eijenson.connpass_searcher.util.getHourOfDay
import jp.eijenson.connpass_searcher.util.isMidnight
import jp.eijenson.connpass_searcher.util.nowCalendar
import jp.eijenson.connpass_searcher.view.content.JobServiceContent
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Created by makoto.kobayashi on 2018/04/24.
 */
class MyJobServicePresenter(private val service: JobServiceContent) : KoinComponent {
    val searchUseCase: SearchUseCase by inject { parametersOf(this) }

    fun onStartJob() {
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

data class Result(
        val id: Long,
        val keyword: String,
        val count: Long
)
