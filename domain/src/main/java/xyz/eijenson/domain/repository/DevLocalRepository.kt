package xyz.eijenson.domain.repository

interface DevLocalRepository {
    fun getLog(): String
    fun d(name: String, text: String)
    fun setLog(text: String)
    fun clear()
}