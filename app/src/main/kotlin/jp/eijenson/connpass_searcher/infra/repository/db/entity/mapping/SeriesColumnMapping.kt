package jp.eijenson.connpass_searcher.infra.repository.db.entity.mapping

import jp.eijenson.connpass_searcher.infra.repository.db.entity.SeriesColumn
import jp.eijenson.model.Series

fun SeriesColumn.toSeries(): Series {
    return Series(id, title, url)
}

fun createSeriesColumn(series: Series): SeriesColumn {
    return series.run { SeriesColumn(0, id, title, url) }
}