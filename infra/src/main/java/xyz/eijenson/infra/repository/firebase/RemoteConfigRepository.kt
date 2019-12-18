package jp.eijenson.connpass_searcher.infra.repository.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import jp.eijenson.connpass_searcher.domain.repository.ConfigRemoteRepository
import xyz.eijenson.infra.BuildConfig
import xyz.eijenson.infra.R

/**
 * Created by kobayashimakoto on 2018/04/16.
 */
class RemoteConfigRepository : ConfigRemoteRepository {
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

    override fun getWelcomeMessage(): String {
        return remoteConfig.getString(WELCOME_MESSAGE)
    }

    override fun fetch() {
        remoteConfig.fetch(1)
        remoteConfig.activateFetched()
    }
}