package jp.eijenson.connpass_searcher.view.data.mapping

import xyz.eijenson.infra.repository.db.AddressGeoCoderRepository
import jp.eijenson.connpass_searcher.view.data.ViewEvent
import jp.eijenson.model.Event
import jp.eijenson.model.Favorite
import jp.eijenson.model.list.FavoriteList

fun Event.toViewEvent(addressGeoCoderRepository: AddressGeoCoderRepository): ViewEvent {
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
        addressGeoCoderRepository.getAddress(this.lat, this.con),
        this.isFavorite
    )
}

fun List<Event>.toViewEventList(addressGeoCoderRepository: AddressGeoCoderRepository): List<ViewEvent> =
    map { it.toViewEvent(addressGeoCoderRepository) }

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
        "",
        true
    )
}

fun FavoriteList.toViewEventList(): List<ViewEvent> = map { it.toViewEvent() }