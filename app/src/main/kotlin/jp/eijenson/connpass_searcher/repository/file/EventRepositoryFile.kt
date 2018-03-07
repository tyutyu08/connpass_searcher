package jp.eijenson.connpass_searcher.repository.file

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Observable
import jp.eijenson.connpass_searcher.repository.EventRepository
import jp.eijenson.connpass_searcher.repository.api.response.ResultEventJson
import jp.eijenson.connpass_searcher.repository.api.response.mapping.toResultEvent
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.model.ResultEvent

class EventRepositoryFile(private val context: Context) : EventRepository {
    override fun getAll(request: RequestEvent): Observable<ResultEvent> {
        val inputStream = context.assets.open("result.json")
        return Observable.create<ResultEvent> {
            it.onNext(inputStream.reader()
                    .use { Gson().fromJson(it, ResultEventJson::class.java).toResultEvent() }
            )
        }
    }
}