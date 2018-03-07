package jp.eijenson.connpass_searcher.repository.local

import io.objectbox.Box
import jp.eijenson.connpass_searcher.repository.column.FavoriteColumn
import jp.eijenson.connpass_searcher.repository.column.FavoriteColumn_
import jp.eijenson.connpass_searcher.repository.column.mapping.createFavoriteColumn
import jp.eijenson.connpass_searcher.repository.column.mapping.toFavoriteList
import jp.eijenson.model.Favorite
import jp.eijenson.model.list.FavoriteList

/**
 * Created by makoto.kobayashi on 2018/03/05.
 */
class FavoriteLocalRepository(private val favoriteTable: Box<FavoriteColumn>) {

    fun insert(favorite: Favorite) {
        favoriteTable.put(createFavoriteColumn(favorite))
    }

    fun delete(eventId: Long) {
        val favorite = favoriteTable.find(FavoriteColumn_.eventId,eventId)
        favoriteTable.remove(favorite)
    }

    fun selectAll(): FavoriteList = favoriteTable.all.toFavoriteList()

    fun contains(id: Long): Boolean = favoriteTable.query().equal(FavoriteColumn_.eventId, id).build().count() > 0
}
