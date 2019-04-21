package jp.eijenson.connpass_searcher.infra.repository.db.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class SeriesColumn(
    @Id var uniqueId: Long,
    val id: Int,
    val title: String,
    val url: String
)