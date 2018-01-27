package eijenson.connpass_searcher.repository.table

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class EventTable(
        @Id var eventId: Long?,
        val title: String?,
        val catchPhrase: String?,
        val description: String?,
        val eventUrl: String?,
        val hashTag: String?,
        val startedAt: String?,
        val endedAt: String?,
        val limit: Int?,
        val eventType: String?,
        //val series: Series?,
        val address: String?,
        val place: String?,
        val lat: Double?,
        val con: Double?,
        val ownerId: Int?,
        val ownerNickname: String?,
        val ownerDisplayName: String?,
        val accepted: Int?,
        val waiting: Int?,
        val updatedAt: String?
)