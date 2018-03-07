package jp.eijenson.connpass_searcher.repository.cache

import jp.eijenson.model.Event

/**
 * Created by makoto.kobayashi on 2018/03/06.
 */
class EventCacheRepository(private val eventList: List<Event>) {
    fun getAll(): List<Event> {
        return eventList
    }

    fun get(id: Long): Event? {
        eventList.map {
            if (it.eventId == id) return it
        }
        return null
    }
}