package jp.eijenson.connpass_searcher.infra.store.sql

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import jp.eijenson.connpass_searcher.infra.repository.db.entity.EventSql

@Database(entities = arrayOf(EventSql::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventSqlStore
}

@Dao
interface EventSqlStore {
    @Query("SELECT * FROM event")
    fun getAll(): List<EventSql>

    @Query("SELECT * FROM event WHERE uid IN (:eventUids)")
    fun loadAllByIds(eventUids: IntArray): List<EventSql>

    @Query("SELECT * FROM event WHERE event_id LIKE :eventid  LIMIT 1")
    fun findByName(eventid: Long): EventSql

    @Insert
    fun insertAll(vararg events: EventSql)

    @Delete
    fun delete(user: EventSql)
}