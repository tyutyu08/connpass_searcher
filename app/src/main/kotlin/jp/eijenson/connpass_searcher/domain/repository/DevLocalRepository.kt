package jp.eijenson.connpass_searcher.domain.repository

interface DevLocalRepository {
    fun getLog(): String
    fun d(name: String, text: String)
    fun setLog(text: String)
    fun clear()
}