package jp.eijenson.connpass_searcher

import android.app.Application
import io.objectbox.BoxStore
import jp.eijenson.connpass_searcher.repository.table.MyObjectBox
import timber.log.Timber

class App : Application() {

    lateinit var boxStore: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()

        boxStore = MyObjectBox.builder().androidContext(this).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}