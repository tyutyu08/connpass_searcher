package jp.eijenson.connpass_searcher.presenter

import io.reactivex.observers.DefaultObserver
import jp.eijenson.connpass_searcher.content.MainContent
import jp.eijenson.connpass_searcher.infra.repository.EventRepository
import jp.eijenson.connpass_searcher.infra.repository.cache.EventCacheRepository
import jp.eijenson.connpass_searcher.infra.repository.db.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.infra.entity.RequestEvent
import jp.eijenson.connpass_searcher.infra.entity.mapping.toSearchHistory
import jp.eijenson.connpass_searcher.infra.repository.local.DevLocalRepository
import jp.eijenson.connpass_searcher.infra.repository.local.SettingsLocalRepository
import jp.eijenson.connpass_searcher.usecase.SearchUseCase
import jp.eijenson.model.Event
import jp.eijenson.model.Favorite
import jp.eijenson.model.ResultEvent
import jp.eijenson.model.SearchHistory
import timber.log.Timber

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MainPresenter(
        private val view: MainContent.View,
        eventRepository: EventRepository,
        private val favoriteLocalRepository: FavoriteLocalRepository,
        private val searchHistoryLocalRepository: SearchHistoryLocalRepository,
        private val devLocalRepository: DevLocalRepository,
        private val settingsLocalRepository: SettingsLocalRepository) : MainContent.Presenter {
    private val eventCacheRepository = EventCacheRepository()
    private lateinit var request: RequestEvent
    private val searchUseCase = SearchUseCase(eventRepository)

    override fun onCreate() {
        if (settingsLocalRepository.enableNotification) {
            view.startJob()
        }
    }

    override fun search(keyword: String, start: Int) {
        request = RequestEvent(keyword = keyword, start = start, prefecture = settingsLocalRepository.prefecture)
        view.visibleProgressBar()
        searchUseCase.search(request, object : DefaultObserver<ResultEvent>() {
            override fun onComplete() {

            }

            override fun onNext(it: ResultEvent) {
                eventCacheRepository.set(it.events)
                val uniqueId = searchHistoryLocalRepository.selectId(request)
                if (uniqueId != null) {
                    searchHistoryLocalRepository.updateSearchDate(uniqueId)
                    val history = searchHistoryLocalRepository.select(uniqueId)!!
                    if (history.saveHistory) {
                        view.goneSaveButton()
                    } else {
                        view.visibleSaveButton(uniqueId)
                    }
                } else {
                    val id = searchHistoryLocalRepository.insert(request.toSearchHistory())
                    view.visibleSaveButton(id)
                }
                view.showSearchResult(checkIsFavorite(it.events), it.resultsAvailable)
                view.goneProgressBar()
            }

            override fun onError(e: Throwable) {
                Timber.d(e)
                view.showSearchErrorToast()
                view.goneProgressBar()
            }

        })
    }

    override fun readMoreSearch(start: Int) {
        request = request.copy(start = start + 1)
        searchUseCase.search(request, object : DefaultObserver<ResultEvent>() {
            override fun onComplete() {

            }

            override fun onNext(it: ResultEvent) {
                eventCacheRepository.set(it.events)
                view.showReadMore(it.events)
            }

            override fun onError(e: Throwable) {
                Timber.d(e)
                view.showSearchErrorToast()
            }

        })
    }

    override fun changedFavorite(favorite: Boolean, itemId: Long) {
        if (favorite) {
            val event = eventCacheRepository.get(itemId) ?: return
            favoriteLocalRepository.insert(Favorite(event))
        } else {
            favoriteLocalRepository.delete(itemId)
        }

    }

    override fun viewDevelopPage() {
        val text = devLocalRepository.getLog()
        view.showDevText(text)
    }

    override fun viewFavoritePage() {
        val favorites = favoriteLocalRepository.selectAll()
        view.showFavoriteList(favorites)
    }

    override fun viewSearchHistoryPage() {
        view.showSearchHistoryList(searchHistoryLocalRepository.selectSavedList())
    }

    override fun selectedSearchHistory(searchHistory: SearchHistory) {
        view.moveToSearchView()
        view.setKeyword(searchHistory.keyword)
        search(searchHistory.keyword)
    }

    override fun onClickDataDelete() {
        searchHistoryLocalRepository.deleteAll()
        favoriteLocalRepository.deleteAll()
        devLocalRepository.clear()
        view.finish()
    }

    override fun onClickSave(searchHistoryId: Long) {
        searchHistoryLocalRepository.updateSaveHistory(searchHistoryId)
        view.goneSaveButton()
    }

    override fun onClickDelete(searchHistory: SearchHistory) {
        searchHistoryLocalRepository.delete(searchHistory)
    }

    override fun onClickDevSwitchApi() {
        view.refreshPresenter(true)
    }

    private fun checkIsFavorite(events: List<Event>): List<Event> {
        return events.map {
            it.isFavorite = favoriteLocalRepository.contains(it.eventId)
            it
        }
    }
}