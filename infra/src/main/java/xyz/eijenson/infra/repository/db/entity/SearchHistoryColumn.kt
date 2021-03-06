package xyz.eijenson.infra.repository.db.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.Date

@Entity
data class SearchHistoryColumn(
    @Id var uniqueId: Long?,
    val eventId: Int?,
    val keyword: String?,
    val keywordOr: String?,
    val ym: Int?,
    val ymd: Int?,
    val nickname: String?,
    val ownerNickname: String?,
    val seriesId: Int?,
    val start: Int?,
    val order: Int?,
    val count: Int?,
    val format: String?,

    var saveHistory: Boolean,
    var searchDate: Date,
    val prefecture: String
)