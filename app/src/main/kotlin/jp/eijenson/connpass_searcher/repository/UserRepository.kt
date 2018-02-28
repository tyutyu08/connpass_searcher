package jp.eijenson.connpass_searcher.repository

/**
 * Created by kobayashimakoto on 2018/02/28.
 */
interface UserRepository {

    fun getFavorites(): List<Long>
    fun saveFavorite(eventId: Long)
}