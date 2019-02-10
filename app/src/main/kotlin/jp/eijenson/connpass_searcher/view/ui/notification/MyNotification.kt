package jp.eijenson.connpass_searcher.view.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_NONE
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.view.ui.activity.MainActivity
import jp.eijenson.connpass_searcher.util.d

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
            description = "保存した検索ワードでの検索結果を通知します"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
        }

        val nm = context.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    fun sendGroupNotification(context: Context) {
        val builder = createBuilder(context)
                .setGroupSummary(true)
                .setGroup("group")
                .setStyle(NotificationCompat.BigTextStyle().setSummaryText("検索結果"))
                .setSmallIcon(R.drawable.ic_search_black_24dp)
                .setNumber(1)
                .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(1, builder.build())
        this.d("sendGroupNotification")

    }

    fun sendNotification(context: Context, id: Int, title: String, text: String, keyword: String) {
        sendGroupNotification(context)
        val builder = createBuilder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_search_black_24dp)
                .setNumber(1)
                .setGroup("group")
                .setAutoCancel(true)

        val intent = MainActivity.createIntent(context, keyword)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent =
                stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(resultPendingIntent)
        NotificationManagerCompat.from(context).notify(id, builder.build())
        this.d("sendNotification")
    }

    private fun createBuilder(context: Context): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, id)
                    .setBadgeIconType(BADGE_ICON_NONE)
        } else {
            NotificationCompat.Builder(context)
        }
    }
}