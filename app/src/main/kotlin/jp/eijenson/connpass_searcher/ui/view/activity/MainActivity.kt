package jp.eijenson.connpass_searcher.ui.view.activity

import android.app.Activity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.repository.EventRepository
import jp.eijenson.connpass_searcher.repository.cache.EventCacheRepository
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import jp.eijenson.connpass_searcher.repository.file.EventRepositoryFile
import jp.eijenson.connpass_searcher.repository.local.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.ui.view.adapter.EventListAdapter
import jp.eijenson.connpass_searcher.ui.view.container.EventList
import jp.eijenson.connpass_searcher.ui.view.container.EventListPage
import jp.eijenson.connpass_searcher.ui.view.data.mapping.toViewEventList
import jp.eijenson.model.Event
import jp.eijenson.model.Favorite
import jp.eijenson.model.list.FavoriteList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.page_develop.view.*
import kotlinx.android.synthetic.main.page_event_list.view.*
import kotlinx.android.synthetic.main.page_favorite_list.view.*
import timber.log.Timber

class MainActivity : Activity(), MainContent.View, EventList.Listener {
    private lateinit var presenter: MainContent.Presenter
    private lateinit var eventListPage: EventListPage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(tool_bar)
        eventListPage = EventListPage(context = this, listener = this)
        // ローカル向き
        presenter = MainPresenter(this,
                EventRepositoryFile(this),
                FavoriteLocalRepository((application as App).favoriteTable))
        // API向き
        //presenter = MainPresenter(this, EventRepositoryImpl(), UserRepositoryLocal(this))
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            if (bottom_navigation.selectedItemId == item.itemId) {
                return@setOnNavigationItemSelectedListener true
            }
            when (item.itemId) {
                R.id.list -> {
                    page.removeAllViews()
                    setupPage()
                }
                R.id.search -> {
                    page.removeAllViews()
                    layoutInflater.inflate(R.layout.page_search_history, page)
                }
                R.id.favorite -> {
                    page.removeAllViews()
                    layoutInflater.inflate(R.layout.page_favorite_list, page)
                    presenter.viewFavoritePage()
                }
                R.id.dev -> {
                    page.removeAllViews()
                    layoutInflater.inflate(R.layout.page_develop, page)
                    presenter.viewDevelopPage()
                }
            }
            true
        }
        setupPage()
        presenter.search()
    }

    override fun showSearchResult(eventList: List<Event>, available: Int) {
        page.tv_search_result_avaliable.text = getString(R.string.search_result_available, available)
        val adapter = object : EventListAdapter(this@MainActivity, eventList.toViewEventList()) {
            override fun onFavoriteChange(favorite: Boolean, itemId: Long) {
                presenter.changedFavorite(favorite, itemId)
            }
        }

        page.list_result.adapter = adapter
    }

    override fun showSearchErrorToast() {
        Toast.makeText(this, "通信失敗", Toast.LENGTH_SHORT).show()
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    override fun actionDone(text: String) {
        presenter.search(text)
    }

    override fun showDevText(text: String) {
        page.tv_dev_1.text = text
    }

    override fun showFavoriteList(favoriteList: FavoriteList) {
        val adapter = object : EventListAdapter(this@MainActivity, favoriteList.toViewEventList()) {
            override fun onFavoriteChange(favorite: Boolean, itemId: Long) {
                presenter.changedFavorite(favorite, itemId)
            }
        }

        page.list_favorite.adapter = adapter
        page.list_favorite.layoutManager = LinearLayoutManager(this)
    }

    private fun setupPage() {
        page.addView(eventListPage, ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ))
    }
}

interface MainContent {
    interface View {
        fun showSearchResult(eventList: List<Event>, available: Int)
        fun showSearchErrorToast()
        fun showToast(text: String)
        fun showDevText(text: String)
        fun showFavoriteList(favoriteList: FavoriteList)

    }

    interface Presenter {
        fun search(keyword: String = "")

        fun changedFavorite(favorite: Boolean, itemId: Long)

        fun viewDevelopPage()

        fun viewFavoritePage()

    }
}

class MainPresenter(
        private val view: MainContent.View,
        private val eventRepository: EventRepository,
        private val favoriteLocalRepository: FavoriteLocalRepository) : MainContent.Presenter {
    private lateinit var eventCacheRepository: EventCacheRepository

    override fun search(keyword: String) {
        Timber.d("keyword=%s", keyword)
        eventRepository.getAll(RequestEvent(keyword = keyword))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeBy(
                        onNext = {
                            eventCacheRepository = EventCacheRepository(it.events)
                            view.showSearchResult(checkIsFavorite(it.events), it.resultsAvailable)
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
        val favorites = favoriteLocalRepository.selectAll()
        view.showDevText(favorites.toString())
    }

    override fun viewFavoritePage() {
        val favorites = favoriteLocalRepository.selectAll()
        view.showFavoriteList(favorites)
    }

    private fun checkIsFavorite(events: List<Event>): List<Event> {
        return events.map {
            it.isFavorite = favoriteLocalRepository.contains(it.eventId)
            it
        }
    }
}