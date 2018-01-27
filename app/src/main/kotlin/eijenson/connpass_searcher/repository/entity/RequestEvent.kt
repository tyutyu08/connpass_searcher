package eijenson.connpass_searcher.repository.entity

import com.google.gson.annotations.SerializedName

data class RequestEvent(
        @SerializedName("event_id") val eventId: Int? = null,
        val keyword: String? = null,
        @SerializedName("keyword_or") val keywordOr: String? = null,
        val ym: Int? = null,
        val ymd: Int? = null,
        val nickname: String? = null,
        @SerializedName("owner_nickname") val ownerNickname: String? = null,
        @SerializedName("series_id") val seriesId: Int? = null,
        val start: Int? = null,
        val order: Int? = null,
        val count: Int? = null,
        val format: String? = null
) {

    fun createParams(): Map<String, String> {
        val params = HashMap<String, String>()
        params.putIfNotNull("event_id", eventId.toString())
        params.putIfNotNull("keyword", keyword)
        params.putIfNotNull("keyword_or", keywordOr)
        params.putIfNotNull("ym", ym.toString())
        params.putIfNotNull("ymd", ymd.toString())
        params.putIfNotNull("nickname", nickname)
        params.putIfNotNull("owner_nickname", ownerNickname)
        params.putIfNotNull("series_id", seriesId.toString())
        params.putIfNotNull("start", start.toString())
        params.putIfNotNull("order", order.toString())
        params.putIfNotNull("count", count.toString())
        params.putIfNotNull("format", format)
        return params
    }

    fun MutableMap<String, String>.putIfNotNull(key: String, value: String?) {
        if (value != null) this.put(key, value)
    }
}