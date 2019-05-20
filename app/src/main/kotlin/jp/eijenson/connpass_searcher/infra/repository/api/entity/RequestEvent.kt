package jp.eijenson.connpass_searcher.infra.repository.api.entity

import com.google.gson.annotations.SerializedName
import java.util.Arrays
import java.util.HashMap

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
    val format: String? = null,

    val prefecture: String = ""
) {

    fun createParams(): Map<String, String> {
        val params = HashMap<String, String>()
        params.putIfNotNull("event_id", eventId)
        val list = Arrays.asList(keyword, prefecture).filterNotNull()
        params.putIfNotNull("keyword", list.joinToString(","))
        params.putIfNotNull("keyword_or", keywordOr)
        params.putIfNotNull("ym", ym)
        params.putIfNotNull("ymd", ymd)
        params.putIfNotNull("nickname", nickname)
        params.putIfNotNull("owner_nickname", ownerNickname)
        params.putIfNotNull("series_id", seriesId)
        params.putIfNotNull("start", start)
        params.putIfNotNull("order", order)
        params.putIfNotNull("count", count)
        params.putIfNotNull("format", format)
        return params
    }

    private fun MutableMap<String, String>.putIfNotNull(key: String, value: String?) {
        if (value != null) this[key] = value
    }

    private fun MutableMap<String, String>.putIfNotNull(key: String, value: Int?) {
        if (value != null) this[key] = value.toString()
    }
}