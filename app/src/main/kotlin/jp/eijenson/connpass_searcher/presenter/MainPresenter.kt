package jp.eijenson.connpass_searcher.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.eijenson.connpass_searcher.content.MainContent
import jp.eijenson.connpass_searcher.repository.EventRepository
import jp.eijenson.connpass_searcher.repository.cache.EventCacheRepository
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.connpass_searcher.repository.local.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.repository.local.SearchHistoryLocalRepository
import jp.eijenson.model.Event
import jp.eijenson.model.Favorite
import jp.eijenson.model.SearchHistory
import timber.log.Timber

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MainPresenter(
        private val view: MainContent.View,
        private val eventRepository: EventRepository,
        private val favoriteLocalRepository: FavoriteLocalRepository,
        private val searchHistoryLocalRepository: SearchHistoryLocalRepository) : MainContent.Presenter {
    private val eventCacheRepository = EventCacheRepository()
    private lateinit var request: RequestEvent

    override fun search(keyword: String, start: Int) {
        request = RequestEvent(keyword = keyword, start = start)
        view.visibleProgressBar()
        eventRepository.getAll(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeBy(
                        onNext = {
                            eventCacheRepository.set(it.events)
                            val uniqueId = searchHistoryLocalRepository.selectId(request)
                            if (uniqueId != null) {
                                val history = searchHistoryLocalRepository.select(uniqueId)!!
                                if (history.saveHistory) {
                                    view.goneSaveButton()
                                } else {
                                    view.visibleSaveButton(uniqueId)
                                }
                            } else {
                                val id = searchHistoryLocalRepository.insert(request)
                                view.visibleSaveButton(id)
                            }
                            view.showSearchResult(checkIsFavorite(it.events), it.resultsAvailable)
                            view.goneProgressBar()
                        },
                        onError = {
                            Timber.d(it)
                            view.showSearchErrorToast()
                            view.goneProgressBar()
                        }
                )
    }

    override fun readMoreSearch(start: Int) {
        request = request.copy(start = start+1)
        eventRepository.getAll(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeBy(
                        onNext = {
                            eventCacheRepository.set(it.events)
                            view.showReadMore(it.events)
                        },
                        onError = {
                            Timber.d(it)
                            view.showSearchErrorToast()
                        }
                )
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
        //val favorites = favoriteLocalRepository.selectAll()
        val history = searchHistoryLocalRepository.selectAll()
        view.showDevText(history.toString())
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

    override fun onClickDev() {
        searchHistoryLocalRepository.deleteAll()
        favoriteLocalRepository.deleteAll()
        view.finish()
    }

    override fun onClickSave(searchHistoryId: Long) {
        searchHistoryLocalRepository.updateSaveHistory(searchHistoryId)
        view.goneSaveButton()
    }

    override fun onClickDelete(searchHistory: SearchHistory) {
        searchHistoryLocalRepository.delete(searchHistory)
    }

    override fun onClickDev2() {
        view.refreshPresenter(true)
    }

    private fun checkIsFavorite(events: List<Event>): List<Event> {
        return events.map {
            it.isFavorite = favoriteLocalRepository.contains(it.eventId)
            it
        }
    }
}