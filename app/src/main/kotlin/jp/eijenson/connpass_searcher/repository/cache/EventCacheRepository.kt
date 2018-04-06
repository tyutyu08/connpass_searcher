package jp.eijenson.connpass_searcher.repository.cache

import jp.eijenson.model.Event

/**
 * Created by makoto.kobayashi on 2018/03/06.
 */
class EventCacheRepository {
    private val eventList = mutableListOf<Event>()

    fun getAll(): List<Event> {
        return eventList
    }

    fun get(id: Long): Event? {
        eventList.map {
            if (it.eventId == id) return it
        }
        return null
    }

    fun set(eventList: List<Event>) {
        this.eventList.addAll(eventList)
    }
}