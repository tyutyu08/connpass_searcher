package jp.eijenson.connpass_searcher.repository.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import jp.eijenson.connpass_searcher.BuildConfig
import jp.eijenson.connpass_searcher.R

/**
 * Created by kobayashimakoto on 2018/04/16.
 */
class RemoteConfigRepository {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    private val WELCOME_MESSAGE = "welcome_message"

    init {
        remoteConfig.setDefaults(R.xml.remote_config_defaults)
        val remoteConfigSettings = FirebaseRemoteConfigSettings
                .Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        remoteConfig.setConfigSettings(remoteConfigSettings)

    }

    fun getWelcomeMessage(): String {
        return remoteConfig.getString(WELCOME_MESSAGE)
    }

    fun fetch(){
        remoteConfig.fetch(1)
        remoteConfig.activateFetched()
    }
}