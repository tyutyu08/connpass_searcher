package jp.eijenson.connpass_searcher.infra.repository.api

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.eijenson.connpass_searcher.domain.repository.AddressLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.EventRemoteRepository
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.connpass_searcher.infra.repository.api.entity.response.mapping.toResultEvent
import jp.eijenson.connpass_searcher.infra.store.api.ApiV1
import jp.eijenson.model.ResultEvent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class EventApiRepository(val addressLocalRepository: AddressLocalRepository) : EventRemoteRepository {
    private val retrofit: Retrofit
    private val eventApi: ApiV1

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://connpass.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        eventApi = retrofit.create(ApiV1::class.java)
    }

    override fun getAll(request: RequestEvent): Single<ResultEvent> {
        return eventApi
            .event(request.createParams())
            .map { it.toResultEvent(addressLocalRepository) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getWhenAfter(request: RequestEvent, date: Date): Single<ResultEvent> {
        return eventApi
            .event(request.createParams())
            .map { json ->
                val resultEvent = json.toResultEvent(addressLocalRepository)
                val list = resultEvent.events.filter { it.updatedAt.after(date) }
                resultEvent.copy(events = list)
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }
}