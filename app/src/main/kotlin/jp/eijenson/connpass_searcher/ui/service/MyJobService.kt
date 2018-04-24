package jp.eijenson.connpass_searcher.ui.service

import android.app.job.JobParameters
import android.app.job.JobService
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.presenter.MyJobServicePresenter
import jp.eijenson.connpass_searcher.presenter.NotificationPresenter
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.repository.local.DevLocalRepository

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MyJobService : JobService() {
    lateinit var presenter: MyJobServicePresenter

    override fun onStartJob(p0: JobParameters?): Boolean {
        val devLocalRepository = DevLocalRepository(this.applicationContext)
        val table = (application as App).searchHistoryTable
        val presenter = MyJobServicePresenter(
                this,
                devLocalRepository,
                SearchHistoryLocalRepository(table))

        presenter.onStartJob()
        return true
    }

    fun notification(keyword: String, count: Int) {
        NotificationPresenter(applicationContext).notifyNewArrival(keyword, count)
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        presenter.onStopJob()
        return true
    }
}