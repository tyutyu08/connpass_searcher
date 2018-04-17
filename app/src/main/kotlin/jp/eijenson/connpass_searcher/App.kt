package jp.eijenson.connpass_searcher

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import jp.eijenson.connpass_searcher.repository.column.FavoriteColumn
import jp.eijenson.connpass_searcher.repository.column.MyObjectBox
import jp.eijenson.connpass_searcher.repository.column.SearchHistoryColumn
import jp.eijenson.connpass_searcher.repository.column.SeriesColumn
import jp.eijenson.connpass_searcher.ui.notification.MyNotification
import jp.eijenson.connpass_searcher.ui.service.MyJobService
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MyNotification().createChannel(applicationContext)
        }
        jobService()
    }

    fun jobService() {
        val componentName = ComponentName(this, MyJobService::class.java)
        val intent = Intent(this, MyJobService::class.java)
        startService(intent)

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1, componentName)
                .setPeriodic(5000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        scheduler.schedule(jobInfo);
    }
}