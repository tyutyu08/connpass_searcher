package jp.eijenson.connpass_searcher.content

/**
 * Created by kobayashimakoto on 2018/05/01.
 */
interface JobServiceContent {
    fun showNotification(id: Int, keyword: String, count: Int)

    fun log(text: String)
}