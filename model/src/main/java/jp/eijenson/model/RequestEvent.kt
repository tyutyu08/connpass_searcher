package jp.eijenson.model

data class RequestEvent(
    val eventId: Int? = null,
    val keyword: String? = null,
    val keywordOr: String? = null,
    val ym: Int? = null,
    val ymd: Int? = null,
    val nickname: String? = null,
    val ownerNickname: String? = null,
    val seriesId: Int? = null,
    val start: Int? = null,
    val order: Int? = null,
    val count: Int? = null,
    val format: String? = null,

    val prefecture: String = ""
)