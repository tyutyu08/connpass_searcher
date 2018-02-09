package eijenson.connpass_searcher.repository.api.response

import com.google.gson.annotations.SerializedName

data class ResultEvent(
        @SerializedName("results_returned") val resultsReturned: Int?,
        @SerializedName("results_available") val resultsAvailable: Int?,
        @SerializedName("results_start") val resultsStart: Int?,
        val events: List<EventJson>?
)