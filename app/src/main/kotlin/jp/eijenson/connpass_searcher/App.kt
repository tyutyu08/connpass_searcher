package jp.eijenson.connpass_searcher

import android.app.Application
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import jp.eijenson.connpass_searcher.repository.column.FavoriteColumn
import jp.eijenson.connpass_searcher.repository.column.MyObjectBox
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.SeriesColumn
import timber.log.Timber

class App : Application() {

    lateinit var favoriteTable: Box<FavoriteColumn>
        private set

    lateinit var seriesTable: Box<SeriesColumn>
        private set

    lateinit var searchHistoryTable: Box<SearchHistoryColumn>

    override fun onCreate() {
        super.onCreate()

        val boxStore = MyObjectBox.builder().androidContext(this).build()
        favoriteTable = boxStore.boxFor()
        seriesTable = boxStore.boxFor()
        searchHistoryTable = boxStore.boxFor()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}