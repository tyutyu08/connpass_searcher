package jp.eijenson.connpass_searcher.view.ui.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import jp.eijenson.connpass_searcher.BuildConfig
import jp.eijenson.connpass_searcher.util.d

/**
 * Created by makoto.kobayashi on 2018/05/08.
 */
class FirstRunJobService : JobService() {

    companion object {
        fun schedule(context: Context) {
            val componentName = ComponentName(context, FirstRunJobService::class.java)

            // デバッグ時は15分,リリース版は6時間ごと
            val periodic = if (BuildConfig.DEBUG) 15 * 60 * 1000L else 6 * 60 * 60 * 1000L
            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(1, componentName)
                .setMinimumLatency(periodic)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()

            scheduler.schedule(jobInfo)
        }
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        this.d("onStartJob")
        startJob()
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    private fun startJob() {
        MyJobService.schedule(this)
    }
}