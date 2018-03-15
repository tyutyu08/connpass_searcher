package jp.eijenson.model

data class SearchHistory(
        val eventId: Int,
        val keyword: String,
        val keywordOr: String,
        val ym: Int,
        val ymd: Int,
        val nickname: String,
        val ownerNickname: String,
        val seriesId: Int,
        val start: Int,
        val order: Int,
        val count: Int,
        val format: String,
        var saveHistory: Boolean
)
