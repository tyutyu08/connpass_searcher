package jp.eijenson.connpass_searcher.infra.repository.db.entity.mapping

import jp.eijenson.connpass_searcher.infra.repository.db.entity.FavoriteColumn
import jp.eijenson.model.Favorite
import jp.eijenson.model.Prefecture
import jp.eijenson.model.list.FavoriteList

fun FavoriteColumn.toFavorite(): Favorite {
    return Favorite(
            eventId,
            title,
            eventUrl,
            startedAt,
            endedAt,
            accepted,
            limit,
            series.target.toSeries(),
            Prefecture.getPreference(prefectureName),
            waiting
    )
}

fun List<FavoriteColumn>.toFavoriteList(): FavoriteList = FavoriteList(map { it.toFavorite() })

fun createFavoriteColumn(favorite: Favorite): FavoriteColumn {
    val favoriteColumn = favorite.run {
        FavoriteColumn(
                0,
                eventId,
                title,
                eventUrl,
                startedAt,
                endedAt,
                accepted,
                limit,
                prefectureName = prefecture.prefectureName,
                waiting = waiting
        )
    }
    favoriteColumn.series.target = createSeriesColumn(favorite.series)
    return favoriteColumn
}
