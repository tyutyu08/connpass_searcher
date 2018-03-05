package jp.eijenson.model

import java.util.*

/**
 * Created by makoto.kobayashi on 2018/03/05.
 */
data class Favorite(
        val eventId: Long,
        val title: String,
        val eventUrl: String,
        val startedAt: Date,
        val endedAt: Date,
        val accepted: Int,
        val limit: Int,
        val series: Series,
        val prefecture: Prefecture
) {
    constructor(event: Event) : this(
            event.eventId,
            event.title,
            event.eventUrl,
            event.startedAt,
            event.endedAt,
            event.accepted,
            event.limit,
            event.series,
            event.prefecture
    )
}