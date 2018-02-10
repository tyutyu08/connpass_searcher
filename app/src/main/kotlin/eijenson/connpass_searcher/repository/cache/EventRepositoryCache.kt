package eijenson.connpass_searcher.repository.cache

import android.content.Context
import com.google.gson.Gson
import eijenson.connpass_searcher.repository.EventRepository
import eijenson.connpass_searcher.repository.api.response.ResultEventJson
import eijenson.connpass_searcher.repository.api.response.mapping.toResultEvent
import eijenson.connpass_searcher.repository.entity.RequestEvent
import eijenson.model.ResultEvent
import io.reactivex.Observable

class EventRepositoryCache(private val context: Context) : EventRepository {
    override fun getEvent(request: RequestEvent): Observable<ResultEvent> {
        val inputStream = context.assets.open("result.json")
        return Observable.create<ResultEvent> {
            it.onNext(inputStream.reader()
                    .use { Gson().fromJson(it, ResultEventJson::class.java).toResultEvent() }
            )
        }
    }
}