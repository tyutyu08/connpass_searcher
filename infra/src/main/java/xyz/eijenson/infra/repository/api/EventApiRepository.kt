package xyz.eijenson.infra.repository.api

import io.reactivex.Single
import jp.eijenson.model.RequestEvent
import jp.eijenson.model.ResultEvent
import jp.eijenson.model.SearchHistory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.eijenson.domain.repository.EventRemoteRepository
import xyz.eijenson.infra.BuildConfig
import xyz.eijenson.infra.api.ApiV1
import xyz.eijenson.infra.repository.api.entity.mapping.toRequestEventJson
import xyz.eijenson.infra.repository.api.entity.response.mapping.toResultEvent
import java.util.Date

class EventApiRepository : EventRemoteRepository {
    private val retrofit: Retrofit
    private val eventApi: ApiV1

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://connpass.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        eventApi = retrofit.create(ApiV1::class.java)
    }

    override fun getAll(requestJson: RequestEvent): Single<ResultEvent> {
        return eventApi.event(requestJson.toRequestEventJson().createParams())
            .map { it.toResultEvent() }
    }

    override fun getWhenAfter(requestJson: RequestEvent, date: Date): Single<ResultEvent> {
        return eventApi
            .event(requestJson.toRequestEventJson().createParams())
            .map { json ->
                val resultEvent = json.toResultEvent()
                val list = resultEvent.events.filter { it.updatedAt.after(date) }
                resultEvent.copy(events = list)
            }
    }

    override fun getWhenAfter(request: SearchHistory, date: Date): Single<ResultEvent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}