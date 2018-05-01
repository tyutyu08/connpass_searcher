package jp.eijenson.connpass_searcher.ui.service

import android.app.job.JobParameters
import android.app.job.JobService
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.content.JobServiceContent
import jp.eijenson.connpass_searcher.presenter.MyJobServicePresenter
import jp.eijenson.connpass_searcher.presenter.NotificationPresenter
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MyJobService : JobService(), JobServiceContent {
    lateinit var presenter: MyJobServicePresenter

    override fun onStartJob(p0: JobParameters?): Boolean {
        val table = (application as App).searchHistoryTable
        presenter = MyJobServicePresenter(
                this,
                SearchHistoryLocalRepository(table))

        presenter.onStartJob()
        return true
    }

    override fun showNotification(keyword: String, count: Int) {
        NotificationPresenter(applicationContext).notifyNewArrival(keyword, count)
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }
}