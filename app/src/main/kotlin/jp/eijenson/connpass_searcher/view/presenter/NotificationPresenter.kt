package jp.eijenson.connpass_searcher.view.presenter

import android.content.Context
import jp.eijenson.connpass_searcher.view.ui.notification.MyNotification

/**
 * Created by makoto.kobayashi on 2018/04/17.
 */
class NotificationPresenter(private val context: Context) {
    private val myNotification = MyNotification()

    fun notifyNewArrival(id: Int, keyword: String, count: Int) {
        val text =
            if (count == 10) {
                keyword + "で検索しました。" + count + "件以上のイベントがあります"
            } else {
                keyword + "で検索しました。" + count + "件のイベントがあります"
            }
        notify(id, "イベント検索結果", text, keyword)
    }

    private fun notify(id: Int, title: String, text: String, keyword: String = "") {
        myNotification.sendNotification(context, id, title, text, keyword)
    }
}