package jp.eijenson.connpass_searcher

import android.app.Application
import android.os.Build
import com.google.firebase.analytics.FirebaseAnalytics
import jp.eijenson.connpass_searcher.di.module.myModule
import jp.eijenson.connpass_searcher.view.ui.notification.MyNotification
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class App : Application() {

    lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(myModule))

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