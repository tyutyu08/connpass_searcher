package jp.eijenson.connpass_searcher.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import jp.eijenson.connpass_searcher.App

/**
 * Created by kobayashimakoto on 2018/05/02.
 */
class FirebaeAnalyticsHelper(private val firebaseAnalytics: FirebaseAnalytics) {

    companion object {
        fun getInstance(): FirebaeAnalyticsHelper {
            return FirebaeAnalyticsHelper(App.app.firebaseAnalytics)
        }
    }

    fun <V1> logEvent(event: Event, key1: Param<V1>, value1: V1) {
        val bundle = Bundle()
        key1.putFunc.put(bundle, key1.key, value1)
        firebaseAnalytics.logEvent(event.toString(), bundle)
    }
}