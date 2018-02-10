package eijenson.connpass_searcher.repository.api

import eijenson.connpass_searcher.repository.api.response.ResultEventJson
import eijenson.connpass_searcher.repository.api.response.mapping.toResultEvent
import eijenson.connpass_searcher.repository.entity.RequestEvent
import eijenson.model.ResultEvent
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

class EventRepositoryImpl {
    private val retrofit: Retrofit
    private val eventApi: api

    init {
        val client = OkHttpClient.Builder().build()
        retrofit = Retrofit.Builder()
                .baseUrl("https://connpass.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        eventApi = retrofit.create(api::class.java)
    }


    fun getEvent(request: RequestEvent): Observable<ResultEvent> {
        return eventApi.event(request.createParams()).map { it.toResultEvent() }
    }

    interface api {
        @GET("/api/v1/event")
        fun event(@QueryMap postMessage: Map<String, String>): Observable<ResultEventJson>
    }
}