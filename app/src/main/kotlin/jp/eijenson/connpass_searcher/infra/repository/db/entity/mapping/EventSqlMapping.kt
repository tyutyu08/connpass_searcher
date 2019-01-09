package jp.eijenson.connpass_searcher.infra.repository.db.entity.mapping

import jp.eijenson.connpass_searcher.infra.repository.db.entity.EventSql
import jp.eijenson.model.Event
import jp.eijenson.model.EventType
import jp.eijenson.model.Prefecture

fun EventSql.transform(): Event {
    return Event(
        eventId,
        title,
        catchPhrase,
        description,
        eventUrl,
        hashTag,
        startedAt,
        endedAt,
        limit,
        EventType.valueOf(eventType),
        series,
        address,
        Prefecture.getPreference(prefecture),
        place,
        lat,
        con,
        ownerId,
        ownerNickname,
        ownerDisplayName,
        accepted,
        waiting,
        updatedAt,
        isFavorite
    )
}

fun Event.transform(): EventSql {
    return EventSql(
        0,
        eventId,
        title,
        catchPhrase,
        description,
        eventUrl,
        hashTag,
        startedAt,
        endedAt,
        limit,
        eventType.name,
        series,
        address,
        prefecture.prefectureName,
        place,
        lat,
        con,
        ownerId,
        ownerNickname,
        ownerDisplayName,
        accepted,
        waiting,
        updatedAt,
        isFavorite
    )
}

@JvmName("eventListTransform")
fun List<Event>.transform(): List<EventSql> {
    return map { it.transform() }
}

@JvmName("eventSqlListTransform")
fun List<EventSql>.transform(): List<Event> {
    return map { it.transform() }
}