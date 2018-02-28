package jp.eijenson.connpass_searcher.repository.local

import android.content.Context
import android.content.SharedPreferences
import jp.eijenson.connpass_searcher.repository.UserRepository

/**
 * Created by kobayashimakoto on 2018/02/28.
 */
class UserRepositoryLocal(context: Context) : UserRepository {
    private val preferenceName = "favoriteList"
    private val favoritesKey = "favorites"
    private val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    }

    override fun getFavorites(): List<Long> {
        val favorites = preferences.getStringSet(favoritesKey, HashSet<String>())
        return favorites.map { it.toLong() }
    }

    override fun saveFavorite(eventId: Long) {
        val favorites = getFavorites()
                .plus(eventId)
                .map { it.toString() }
                .toMutableSet()
        preferences.edit()
                .putStringSet(favoritesKey, favorites)
                .apply()
    }

    override fun removeFavorite(eventId: Long) {
        val favorites = getFavorites()
                .minus(eventId)
                .map { it.toString() }
                .toMutableSet()
        preferences.edit()
                .putStringSet(favoritesKey, favorites)
                .apply()
    }

}