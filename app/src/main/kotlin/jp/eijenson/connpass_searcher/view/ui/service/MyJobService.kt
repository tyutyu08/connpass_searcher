package jp.eijenson.connpass_searcher.view.ui.service

import android.app.job.JobParameters
import android.app.job.JobService
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.analytics.Event
import jp.eijenson.connpass_searcher.analytics.FirebaeAnalyticsHelper
import jp.eijenson.connpass_searcher.analytics.Param
import jp.eijenson.connpass_searcher.view.content.JobServiceContent
import jp.eijenson.connpass_searcher.view.presenter.MyJobServicePresenter
import jp.eijenson.connpass_searcher.view.presenter.NotificationPresenter
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.util.d
import jp.eijenson.connpass_searcher.util.nowString

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MyJobService : JobService(), JobServiceContent {
    private lateinit var presenter: MyJobServicePresenter

    override fun onStartJob(p0: JobParameters?): Boolean {
        this.d("onStartJob")
        val table = (application as App).searchHistoryTable
        presenter = MyJobServicePresenter(
                this,
                SearchHistoryLocalRepository(table))

        presenter.onStartJob()
        FirebaeAnalyticsHelper.getInstance().logEvent(Event.JOB_START,
                Param.TIME, nowString())
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