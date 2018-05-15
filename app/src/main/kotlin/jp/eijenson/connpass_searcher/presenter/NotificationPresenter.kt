package jp.eijenson.connpass_searcher.presenter

import android.content.Context
import jp.eijenson.connpass_searcher.ui.notification.MyNotification

/**
 * Created by makoto.kobayashi on 2018/04/17.
 */
class NotificationPresenter(private val context: Context) {
    private val myNotification = MyNotification()

    fun notifyTest() {
        notify(99, "新しいタイトル", "テキスト")
    }

    fun notifyNewArrival(id: Int, keyword: String, count: Int) {
        notify(id, "イベント検索結果", keyword + "で検索しました。" + count + "件のイベントがあります", keyword)
    }

    private fun notify(id: Int, title: String, text: String, keyword: String = "") {
        myNotification.sendNotification(context, id, title, text, keyword)
    }
}