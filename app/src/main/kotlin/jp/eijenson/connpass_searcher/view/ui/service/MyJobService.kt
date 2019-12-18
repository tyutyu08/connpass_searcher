package jp.eijenson.connpass_searcher.view.ui.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.BuildConfig
import jp.eijenson.connpass_searcher.analytics.Event
import jp.eijenson.connpass_searcher.analytics.FirebaeAnalyticsHelper
import jp.eijenson.connpass_searcher.analytics.Param
import jp.eijenson.connpass_searcher.di.module.ServiceModule
import jp.eijenson.connpass_searcher.util.d
import jp.eijenson.connpass_searcher.util.nowString
import jp.eijenson.connpass_searcher.view.content.JobServiceContent
import jp.eijenson.connpass_searcher.view.presenter.NotificationPresenter
import javax.inject.Inject

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MyJobService : JobService(), JobServiceContent.View {
    @Inject
    lateinit var presenter: JobServiceContent.Presenter

    companion object {
        fun schedule(context: Context) {
            val componentName = ComponentName(context, MyJobService::class.java)

            // デバッグ時は15分,リリース版は6時間ごと
            val periodic = if (BuildConfig.DEBUG) 15 * 60 * 1000L else 6 * 60 * 60 * 1000L
            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(2, componentName)
                .setPeriodic(periodic)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()

            scheduler.schedule(jobInfo)
        }
    }

    override fun onCreate() {
        super.onCreate()
        App.app.appComponent.plus(ServiceModule(this)).inject(this)
        this.d("presenter$presenter")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        this.d("onStartJob")

        presenter.onStartJob()
        FirebaeAnalyticsHelper.getInstance().logEvent(
            Event.JOB_START,
            Param.TIME, nowString()
        )
        return true
    }

    override fun showNotification(id: Int, keyword: String, count: Int) {
        this.d("sendNotification")
        NotificationPresenter(this).notifyNewArrival(id, keyword, count)
    }

    override fun log(text: String) {
        this.d(text)
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        this.d("onStopJob")
        return true
    }
}