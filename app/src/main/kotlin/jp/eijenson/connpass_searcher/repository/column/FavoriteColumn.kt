package jp.eijenson.connpass_searcher.repository.column

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/03/05.
 */
@Entity
data class FavoriteColumn(
        @Id var uniqueKey: Long,
        val eventId: Long,
        val title: String,
        val eventUrl: String,
        val startedAt: Date,
        val endedAt: Date,
        val accepted: Int,
        val limit: Int,
        val seriesId: Long,
        val prefectureName: String,
        val waiting: Int
)
