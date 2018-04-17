package jp.eijenson.connpass_searcher.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import jp.eijenson.connpass_searcher.R

/**
 * Created by makoto.kobayashi on 2018/04/17.
 */
class MyNotification {
    val id = "testChannel"

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createChannel(context: Context) {
        val id = "testChannel"
        val name = "テストチャンネル"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        channel.run {
            description = "テストちゃんねるの説明"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
        }

        val nm = context.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun sendNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, id)
                .setContentTitle("tesuto")
                .setContentText("vivivi")
                .setNumber(1)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
        NotificationManagerCompat.from(context).notify(1, builder.build())
    }
}