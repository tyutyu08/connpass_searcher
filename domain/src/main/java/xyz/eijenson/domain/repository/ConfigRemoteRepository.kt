package xyz.eijenson.domain.repository

interface ConfigRemoteRepository {
    fun getWelcomeMessage(): String
    fun fetch()
}