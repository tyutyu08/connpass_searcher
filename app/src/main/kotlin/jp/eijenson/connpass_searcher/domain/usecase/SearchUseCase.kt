package jp.eijenson.connpass_searcher.domain.usecase

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.connpass_searcher.domain.repository.EventRemoteRepository
import jp.eijenson.model.ResultEvent
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class SearchUseCase(private val eventRemoteRepository: EventRemoteRepository) {

    fun search(request: RequestEvent, subscribe: Observer<ResultEvent>) {
        eventRemoteRepository.getAll(request)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscribe)
    }

    fun countNewEvent(resultEvent: ResultEvent, searchDate: Date): Int {
        var count = 0
        resultEvent.events.forEach { event ->
            if (event.updatedAt.after(searchDate)) {
                count++
            }
        }
        return count
    }

}