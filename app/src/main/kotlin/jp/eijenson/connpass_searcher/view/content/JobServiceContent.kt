package jp.eijenson.connpass_searcher.view.content

/**
 * Created by kobayashimakoto on 2018/05/01.
 */
interface JobServiceContent {
    interface View {
        fun showNotification(id: Int, keyword: String, count: Int)

        fun log(text: String)
    }

    interface Presenter {
        fun onStartJob()
    }
}