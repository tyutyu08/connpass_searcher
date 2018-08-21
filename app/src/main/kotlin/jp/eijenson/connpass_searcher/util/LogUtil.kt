package jp.eijenson.connpass_searcher.util

import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.BuildConfig
import jp.eijenson.connpass_searcher.infra.repository.local.DevLocalRepository

/**
 * Created by kobayashimakoto on 2018/05/02.
 */
fun Any.d(text: String){
    if (!BuildConfig.DEBUG) return
    val devLocalRepository = DevLocalRepository(App.app)
    devLocalRepository.d(this.javaClass.simpleName, text)
}