package jp.eijenson.connpass_searcher.infra.repository.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.eijenson.model.Series
import java.util.Date

@Entity(tableName = "event")
data class EventSql(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "event_id") val eventId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "catch_phrase") val catchPhrase: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "event_url") val eventUrl: String,
    @ColumnInfo(name = "hash_tag") val hashTag: String,
    @ColumnInfo(name = "started_at") val startedAt: Date,
    @ColumnInfo(name = "ended_at") val endedAt: Date,
    @ColumnInfo(name = "limit") val limit: Int,
    @ColumnInfo(name = "event_type") val eventType: String,
    @ColumnInfo(name = "series") val series: Series,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "prefecture") val prefecture: String,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "con") val con: Double,
    @ColumnInfo(name = "owner_id") val ownerId: Int,
    @ColumnInfo(name = "owner_nick_name") val ownerNickname: String,
    @ColumnInfo(name = "owner_display_name") val ownerDisplayName: String,
    @ColumnInfo(name = "accepted") val accepted: Int,
    @ColumnInfo(name = "waiting") val waiting: Int,
    @ColumnInfo(name = "update_at") val updatedAt: Date,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean
)