package jp.eijenson.connpass_searcher.domain.repository

import jp.eijenson.model.Favorite
import jp.eijenson.model.list.FavoriteList

interface FavoriteLocalRepository {
    fun insert(favorite: Favorite)
    fun delete(eventId: Long)
    fun deleteAll()
    fun selectAll(): FavoriteList
    fun contains(id: Long): Boolean
}