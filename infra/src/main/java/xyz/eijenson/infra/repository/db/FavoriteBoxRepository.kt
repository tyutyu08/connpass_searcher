package xyz.eijenson.infra.repository.db

import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import xyz.eijenson.domain.repository.FavoriteLocalRepository
import xyz.eijenson.infra.repository.db.entity.FavoriteColumn
import jp.eijenson.connpass_searcher.infra.repository.db.entity.FavoriteColumn_
import xyz.eijenson.infra.repository.db.entity.mapping.createFavoriteColumn
import xyz.eijenson.infra.repository.db.entity.mapping.toFavoriteList
import jp.eijenson.model.Favorite
import jp.eijenson.model.list.FavoriteList

/**
 * Created by makoto.kobayashi on 2018/03/05.
 */
class FavoriteBoxRepository(
    boxStore: BoxStore
) : FavoriteLocalRepository {
    private val favoriteTable: Box<FavoriteColumn> = boxStore.boxFor()

    override fun insert(favorite: Favorite) {
        favoriteTable.put(createFavoriteColumn(favorite))
    }

    override fun delete(eventId: Long) {
        favoriteTable
            .query()
            .equal(FavoriteColumn_.eventId, eventId)
            .build()
            .remove()
    }

    override fun deleteAll() {
        favoriteTable.removeAll()
    }

    override fun selectAll(): FavoriteList = favoriteTable.all.toFavoriteList()

    override fun contains(id: Long): Boolean =
        favoriteTable.query().equal(FavoriteColumn_.eventId, id).build().count() > 0
}
