package jp.eijenson.connpass_searcher.infra.repository.api

import com.google.gson.Gson
import io.reactivex.Single
import jp.eijenson.connpass_searcher.domain.repository.EventRemoteRepository
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.connpass_searcher.infra.repository.api.entity.response.ResultEventJson
import jp.eijenson.connpass_searcher.infra.repository.api.entity.response.mapping.toResultEvent
import jp.eijenson.model.ResultEvent
import java.io.File
import java.util.Date

class EventTestRepository : EventRemoteRepository {
    override fun getWhenAfter(request: RequestEvent, date: Date): Single<ResultEvent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(request: RequestEvent): Single<ResultEvent> {
        val file = File(javaClass.classLoader!!.getResource("result.json")!!.path)
        return Single.create<ResultEvent> {
            it.onSuccess(file.reader()
                .use { Gson().fromJson(it, ResultEventJson::class.java).toResultEvent() }
            )
        }
    }
}