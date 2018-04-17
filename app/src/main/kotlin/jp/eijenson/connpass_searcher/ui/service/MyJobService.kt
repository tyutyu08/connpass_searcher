package jp.eijenson.connpass_searcher.ui.service

import android.app.job.JobParameters
import android.app.job.JobService
import jp.eijenson.connpass_searcher.repository.local.DevLocalRepository
import java.util.*

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MyJobService : JobService() {
    override fun onStartJob(p0: JobParameters?): Boolean {
        val devRepository = DevLocalRepository(this.applicationContext)
        val text = devRepository.getText()+ "Start : " + Date() + "\n"
        devRepository.setText(text)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        val devRepository = DevLocalRepository(this.applicationContext)
        val text = devRepository.getText()+ "End : " + Date() + "\n"
        devRepository.setText(text)
        return true
    }
}