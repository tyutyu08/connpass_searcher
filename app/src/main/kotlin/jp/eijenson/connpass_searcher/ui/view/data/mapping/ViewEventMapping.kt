package jp.eijenson.connpass_searcher.ui.view.data.mapping

import jp.eijenson.connpass_searcher.ui.view.data.ViewEvent
import jp.eijenson.model.Event
import jp.eijenson.model.Favorite
import jp.eijenson.model.list.FavoriteList

fun Event.toViewEvent(): ViewEvent {
    return ViewEvent(
            this.eventId,
            this.title,
            this.eventUrl,
            this.startedAt,
            this.endedAt,
            this.accepted,
            this.limit,
            this.series,
            this.prefecture,
            this.waiting,
            this.isFavorite
    )
}

fun List<Event>.toViewEventList(): List<ViewEvent> = map { it.toViewEvent() }

fun Favorite.toViewEvent(): ViewEvent {
    return ViewEvent(
            this.eventId,
            this.title,
            this.eventUrl,
            this.startedAt,
            this.endedAt,
            this.accepted,
            this.limit,
            this.series,
            this.prefecture,
            this.waiting,
            true
    )
}

fun FavoriteList.toViewEventList(): List<ViewEvent> = map { it.toViewEvent() }