package jp.eijenson.connpass_searcher.ui.notification

import android.app.Notification
import android.app.Notification.BADGE_ICON_NONE
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
        val name = "検索結果通知"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance).apply {
            description = "保存した検索ワードでの新規イベントを通知します"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
        }

        val nm = context.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun sendNotification(context: Context, title: String, text: String) {
        val builder = NotificationCompat.Builder(context, id)
                .setContentTitle(title)
                .setContentText(text)
                .setNumber(1)
                .setBadgeIconType(BADGE_ICON_NONE)
                .setSmallIcon(R.drawable.ic_search_black_24dp)
        NotificationManagerCompat.from(context).notify(1, builder.build())
    }

    fun sendNotificationOld(context: Context, title: String, text: String) {
        val builder = NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setNumber(1)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
        NotificationManagerCompat.from(context).notify(1, builder.build())
    }
}