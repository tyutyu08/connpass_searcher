package jp.eijenson.connpass_searcher.ui.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import jp.eijenson.connpass_searcher.BuildConfig

/**
 * Created by makoto.kobayashi on 2018/05/08.
 */
class FirstRunJobService : JobService() {
    override fun onStartJob(p0: JobParameters?): Boolean {
        startJob()
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    private fun startJob() {
        val componentName = ComponentName(this, MyJobService::class.java)
        //val intent = Intent(this, MyJobService::class.java)
        //startService(intent)

        // デバッグ時は15分,リリース版は6時間ごと
        val periodic = if (BuildConfig.DEBUG) 15 * 60 * 1000L else 6 * 60 * 60 * 1000L
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(2, componentName)
                .setPeriodic(periodic)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()

        scheduler.schedule(jobInfo)
    }
}