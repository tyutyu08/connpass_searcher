package jp.eijenson.connpass_searcher.infra.repository.db

import android.content.Context
import androidx.room.Room
import jp.eijenson.connpass_searcher.infra.repository.db.entity.mapping.transform
import jp.eijenson.connpass_searcher.infra.store.sql.AppDatabase
import jp.eijenson.model.Event

class EventSqlRepository(context: Context) {
    val db by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "event"
        ).build()
    }

    fun getAll(): List<Event> {
        return db.eventDao().getAll().transform()
    }

    fun insertList(events:List<Event>){
        db.eventDao().insertAll(*events.transform().toTypedArray())
    }

    fun deleteAll(){
        db.clearAllTables()
    }
}