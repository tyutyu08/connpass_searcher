package jp.eijenson.connpass_searcher.repository.table.mapping

import jp.eijenson.connpass_searcher.repository.table.FavoriteColumn
import jp.eijenson.model.Favorite
import jp.eijenson.model.Prefecture
import jp.eijenson.model.Series
import jp.eijenson.model.list.FavoriteList

/**
 * Created by makoto.kobayashi on 2018/03/05.
 */
fun FavoriteColumn.toFavorite(): Favorite {
    return Favorite(
            eventId,
            title,
            eventUrl,
            startedAt,
            endedAt,
            accepted,
            limit,
            Series(seriesId.toInt(), "test", "test"),
            Prefecture.getPreference(prefectureName)
    )
}

fun List<FavoriteColumn>.toFavoriteList(): FavoriteList = FavoriteList(map { it.toFavorite() })

fun createFavoriteColumn(favorite: Favorite): FavoriteColumn {
    return favorite.let {
        FavoriteColumn(
                0,
                it.eventId,
                it.title,
                it.eventUrl,
                it.startedAt,
                it.endedAt,
                it.accepted,
                it.limit,
                it.series.id.toLong(),
                it.prefecture.prefectureName
        )
    }
}
