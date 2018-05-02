package jp.eijenson.connpass_searcher.util

import android.content.Context
import jp.eijenson.connpass_searcher.repository.local.DevLocalRepository

/**
 * Created by kobayashimakoto on 2018/05/02.
 */
fun Context.d(text: String) {
    val devLocalRepository = DevLocalRepository(this)
    devLocalRepository.d(this.javaClass.simpleName, text)
}