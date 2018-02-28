package jp.eijenson.model

import java.util.*

data class Event(
        val eventId: Long,
        val title: String,
        val catchPhrase: String,
        val description: String,
        val eventUrl: String,
        val hashTag: String,
        val startedAt: Date,
        val endedAt: Date,
        val limit: Int,
        val eventType: EventType,
        val series: Series,
        val address: String,
        val prefecture: Prefecture,
        val place: String,
        val lat: Double,
        val con: Double,
        val ownerId: Int,
        val ownerNickname: String,
        val ownerDisplayName: String,
        val accepted: Int,
        val waiting: Int,
        val updatedAt: Date,
        var isFavorite: Boolean
)