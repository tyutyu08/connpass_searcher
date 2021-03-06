package xyz.eijenson.infra.repository.db.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.Date

/**
 * Created by makoto.kobayashi on 2018/03/05.
 */
@Entity
data class FavoriteColumn(
    @Id var uniqueKey: Long = 0,
    val eventId: Long = -1,
    val title: String = "",
    val eventUrl: String = "",
    val startedAt: Date = Date(0),
    val endedAt: Date = Date(0),
    val accepted: Int = -1,
    val limit: Int = -1,
    val prefectureName: String = "",
    val waiting: Int = -1
) {
    lateinit var series: ToOne<SeriesColumn>
}
