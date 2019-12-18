package xyz.eijenson.domain.repository

interface SettingsLocalRepository {
    var prefecture: String
    var enableNotification: Boolean
}