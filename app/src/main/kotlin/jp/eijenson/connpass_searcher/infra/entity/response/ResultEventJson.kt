package jp.eijenson.connpass_searcher.infra.entity.response

import com.google.gson.annotations.SerializedName

data class ResultEventJson(
        @SerializedName("results_returned") val resultsReturned: Int?,
        @SerializedName("results_available") val resultsAvailable: Int?,
        @SerializedName("results_start") val resultsStart: Int?,
        val events: List<EventJson>?
)