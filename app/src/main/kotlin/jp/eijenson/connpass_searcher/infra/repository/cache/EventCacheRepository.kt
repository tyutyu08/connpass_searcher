package jp.eijenson.connpass_searcher.infra.repository.cache

import jp.eijenson.connpass_searcher.domain.repository.EventLocalRepository
import jp.eijenson.model.Event

/**
 * Created by makoto.kobayashi on 2018/03/06.
 */
class EventCacheRepository : EventLocalRepository {
    private val eventList = mutableListOf<Event>()

    override fun get(id: Long): Event? {
        eventList.map {
            if (it.eventId == id) return it
        }
        return null
    }

    override fun set(eventList: List<Event>) {
        this.eventList.addAll(eventList)
    }
}