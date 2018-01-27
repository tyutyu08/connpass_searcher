package eijenson.connpass_searcher.repository.cache

import eijenson.connpass_searcher.App
import eijenson.connpass_searcher.repository.table.EventTable

class EventRepositoryCache(app: App){
    val eventBox =  app.boxStore.boxFor(EventTable::class.java)

    fun insert(eventTable: EventTable){
        eventBox.put(eventTable)
    }

}