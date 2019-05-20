package jp.eijenson.connpass_searcher.view.ui.container.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.eijenson.connpass_searcher.domain.repository.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.view.data.ViewEvent
import jp.eijenson.connpass_searcher.view.data.mapping.toFavorite
import jp.eijenson.model.list.FavoriteList

class FavoriteViewModel(
        private val favoriteRepository: FavoriteLocalRepository
) : ViewModel() {

    val favoriteList = MutableLiveData<FavoriteList>()

    fun onCreate() {
        favoriteList.value = favoriteRepository.selectAll()
    }

    fun changedFavorite(favorite: Boolean, item: ViewEvent) {
        if (favorite) {
            favoriteRepository.insert(item.toFavorite())
        } else {
            favoriteRepository.delete(item.eventId)
        }
    }

    class Factory(
            private val favoriteRepository: FavoriteLocalRepository
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FavoriteViewModel(favoriteRepository) as T
        }
    }
}
