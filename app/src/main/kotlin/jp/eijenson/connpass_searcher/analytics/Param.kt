package jp.eijenson.connpass_searcher.analytics

import android.os.Bundle

/**
 * 各ログ収集基盤に送信するパラメータ名と型を定義する
 */

class Param<T> private constructor(internal var key: String, internal var putFunc: IFunc<T>) {
    companion object {
        val TIME = Param("time", object : IFunc<String> {
            override fun put(dest: Bundle, key: String, value: String) {
                dest.getString(key, value)
            }
        })
    }

    interface IFunc<V> {
        fun put(dest: Bundle, key: String, value: V)
    }
}
