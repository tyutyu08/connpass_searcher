package jp.eijenson.connpass_searcher.presenter

import android.content.Context
import android.os.Build
import jp.eijenson.connpass_searcher.ui.notification.MyNotification

/**
 * Created by makoto.kobayashi on 2018/04/17.
 */
class NotificationPresenter(private val context: Context) {
    private val myNotification = MyNotification()

    fun notifyTest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myNotification.sendNotification(context,"新しいタイトル","テキスト")
        } else {
            myNotification.sendNotificationOld(context,"古いタイトル","テキスト")
        }
    }
}