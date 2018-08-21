package jp.eijenson.connpass_searcher

import android.app.Application
import android.os.Build
import com.google.firebase.analytics.FirebaseAnalytics
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import jp.eijenson.connpass_searcher.infra.repository.column.FavoriteColumn
import jp.eijenson.connpass_searcher.infra.repository.column.MyObjectBox
import jp.eijenson.connpass_searcher.infra.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.infra.repository.column.SeriesColumn
import jp.eijenson.connpass_searcher.view.ui.notification.MyNotification
import timber.log.Timber

class App : Application() {

    lateinit var favoriteTable: Box<FavoriteColumn>
        private set

    private lateinit var seriesTable: Box<SeriesColumn>

    lateinit var searchHistoryTable: Box<SearchHistoryColumn>

    lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()

        val boxStore = MyObjectBox.builder().androidContext(this).build()
        favoriteTable = boxStore.boxFor()
        seriesTable = boxStore.boxFor()
        searchHistoryTable = boxStore.boxFor()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MyNotification().createChannel(applicationContext)
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        app = this
    }
}