package jp.eijenson.connpass_searcher.domain.repository

interface SettingsLocalRepository {
    var prefecture: String
    var enableNotification: Boolean
}