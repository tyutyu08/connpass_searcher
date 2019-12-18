package jp.eijenson.connpass_searcher.view.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.eijenson.connpass_searcher.domain.repository.DevLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.SettingsLocalRepository
import jp.eijenson.connpass_searcher.domain.usecase.SearchUseCase
import jp.eijenson.connpass_searcher.infra.repository.api.entity.mapping.toRequestEventJson
import jp.eijenson.connpass_searcher.infra.repository.api.entity.mapping.toSearchHistory
import jp.eijenson.connpass_searcher.infra.repository.cache.EventCacheRepository
import jp.eijenson.connpass_searcher.view.content.MainContent
import jp.eijenson.model.Event
import jp.eijenson.model.Favorite
import jp.eijenson.model.RequestEvent
import jp.eijenson.model.SearchHistory
import timber.log.Timber

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
class MainPresenter(
    private val view: MainContent.View,
    private val searchUseCase: SearchUseCase,
    private val favoriteBoxRepository: FavoriteLocalRepository,
    private val searchHistoryLocalRepository: SearchHistoryLocalRepository,
    private val devLocalRepository: DevLocalRepository,
    private val settingsLocalRepository: SettingsLocalRepository
) : MainContent.Presenter {

    private val eventCacheRepository = EventCacheRepository()
    private lateinit var request: RequestEvent

    override fun onCreate() {
        if (settingsLocalRepository.enableNotification) {
            view.startJob()
        }
    }

    override fun search(keyword: String, start: Int) {
        request = RequestEvent(
            keyword = keyword,
            start = start,
            prefecture = settingsLocalRepository.prefecture
        )
        view.visibleProgressBar()
        searchUseCase
            .search(request)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
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
                        val id =
                            searchHistoryLocalRepository.insert(request.toRequestEventJson().toSearchHistory())
                        view.visibleSaveButton(id)
                    }
                    view.showSearchResult(checkIsFavorite(it.events), it.resultsAvailable)
                    view.goneProgressBar()
                },
                onError = { e ->
                    Timber.d(e)
                    view.showSearchErrorToast()
                    view.goneProgressBar()
                }
            )
    }

    override fun readMoreSearch(start: Int) {
        request = request.copy(start = start + 1)
        searchUseCase.search(request).subscribeBy(
            onError = { e ->
                Timber.d(e)
                view.showSearchErrorToast()
            },
            onSuccess = {
                eventCacheRepository.set(it.events)
                view.showReadMore(it.events)
            }
        )
    }

    override fun changedFavorite(favorite: Boolean, itemId: Long) {
        if (favorite) {
            val event = eventCacheRepository.get(itemId) ?: return
            favoriteBoxRepository.insert(Favorite(event))
        } else {
            favoriteBoxRepository.delete(itemId)
        }
    }

    override fun viewDevelopPage() {
        val text = devLocalRepository.getLog()
        view.showDevText(text)
    }

    override fun viewFavoritePage() {
        val favorites = favoriteBoxRepository.selectAll()
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
        favoriteBoxRepository.deleteAll()
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
        //view.refreshPresenter(true)
    }

    private fun checkIsFavorite(events: List<Event>): List<Event> {
        return events.map {
            it.isFavorite = favoriteBoxRepository.contains(it.eventId)
            it
        }
    }
}