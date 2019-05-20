package jp.eijenson.connpass_searcher.util

import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.BuildConfig
import jp.eijenson.connpass_searcher.infra.repository.local.DevSharedRepository

/**
 * Created by kobayashimakoto on 2018/05/02.
 */
fun Any.d(text: String) {
    if (!BuildConfig.DEBUG) return
    val devLocalRepository = DevSharedRepository(App.app)
    devLocalRepository.d(this.javaClass.simpleName, text)
}