package jp.eijenson.connpass_searcher.infra.entity.response.mapping

import jp.eijenson.connpass_searcher.infra.entity.response.EventJson
import jp.eijenson.connpass_searcher.infra.entity.response.ResultEventJson
import jp.eijenson.connpass_searcher.infra.entity.response.SeriesJson
import jp.eijenson.model.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun ResultEventJson.toResultEvent(): ResultEvent {
    return ResultEvent(
            this.resultsReturned ?: -1,
            this.resultsAvailable ?: -1,
            this.resultsStart ?: -1,
            this.events?.toEventList() ?: emptyList()
    )
}

fun List<EventJson>.toEventList(): List<Event> {
    return map {
        Event(
                it.eventId ?: -1,
                it.title ?: "",
                it.catchPhrase ?: "",
                it.description ?: "",
                it.eventUrl ?: "",
                it.hashTag ?: "",
                it.startedAt?.toDate() ?: Date(0),
                it.endedAt?.toDate() ?: Date(0),
                it.limit ?: -1,
                it.eventType?.toEventType() ?: EventType.UNDEFINED,
                it.series?.toSeries() ?: Series(-1, "", ""),
                it.address ?: "",
                it.address?.toPrefecture() ?: Prefecture.UNDEFINED,
                it.place ?: "",
                it.lat ?: -1.0,
                it.lon ?: -1.0,
                it.ownerId ?: -1,
                it.ownerNickname ?: "",
                it.ownerDisplayName ?: "",
                it.accepted ?: -1,
                it.waiting ?: -1,
                it.updatedAt?.toDate() ?: Date(0),
                false
        )
    }
}

private fun String.toPrefecture(): Prefecture {
    return Prefecture.getPreference(this)
}

private fun String.toDate(): Date? {
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.JAPAN)

    try {
        return df.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return null
}

private fun EventJson.EventType.toEventType(): EventType = when (this) {
    EventJson.EventType.ADVERTISEMENT -> EventType.ADVERTISEMENT
    EventJson.EventType.PARTICIPATION -> EventType.PARTICIPATION
}

private fun SeriesJson.toSeries(): Series {
    return Series(
            this.id ?: -1,
            this.title ?: "",
            this.url ?: ""
    )
}