package xyz.eijenson.domain.repository

import jp.eijenson.model.Event

interface EventLocalRepository {
    fun get(id: Long): Event?
    fun set(eventList: List<Event>)
}