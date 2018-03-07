package jp.eijenson.model.list

import jp.eijenson.model.Favorite

/**
 * Created by makoto.kobayashi on 2018/03/06.
 */
class FavoriteList(private val favoriteList: List<Favorite>) : Iterable<Favorite> {

    override fun iterator() = favoriteList.iterator()
}