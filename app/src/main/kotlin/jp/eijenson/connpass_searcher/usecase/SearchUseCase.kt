package jp.eijenson.connpass_searcher.usecase

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.eijenson.connpass_searcher.repository.EventRepository
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.model.ResultEvent

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class SearchUseCase(private val eventRepository: EventRepository) {
    private lateinit var request: RequestEvent

    fun search(keyword: String, start: Int, subscribe: Observer<ResultEvent>) {
        request = RequestEvent(keyword = keyword, start = start)
        eventRepository.getAll(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(subscribe)
    }

}