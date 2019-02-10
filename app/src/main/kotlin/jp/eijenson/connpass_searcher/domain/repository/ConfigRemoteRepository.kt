package jp.eijenson.connpass_searcher.domain.repository

interface ConfigRemoteRepository {
    fun getWelcomeMessage(): String
    fun fetch()
}