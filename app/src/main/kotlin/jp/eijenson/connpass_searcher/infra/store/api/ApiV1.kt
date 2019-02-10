package jp.eijenson.connpass_searcher.infra.store.api

import io.reactivex.Single
import jp.eijenson.connpass_searcher.infra.repository.api.entity.response.ResultEventJson
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiV1 {
    @GET("/api/v1/event")
    fun event(@QueryMap postMessage: Map<String, String>): Single<ResultEventJson>
}
