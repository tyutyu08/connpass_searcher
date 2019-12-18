package jp.eijenson.connpass_searcher

import android.app.Application
import android.os.Build
import com.google.firebase.analytics.FirebaseAnalytics
import jp.eijenson.connpass_searcher.di.module.AppComponent
import jp.eijenson.connpass_searcher.di.module.AppModule
import jp.eijenson.connpass_searcher.di.module.DaggerAppComponent
import jp.eijenson.connpass_searcher.view.ui.notification.MyNotification
import timber.log.Timber

class App : Application() {

    lateinit var firebaseAnalytics: FirebaseAnalytics

    lateinit var appComponent: AppComponent

    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()

        appComponent =
            DaggerAppComponent.builder().appModule(AppModule(this)).build().also { it.inject(this) }

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