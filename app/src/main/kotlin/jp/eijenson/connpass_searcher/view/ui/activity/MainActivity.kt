package jp.eijenson.connpass_searcher.view.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import jp.eijenson.connpass_searcher.BuildConfig
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.infra.repository.db.AddressGeoCoderRepository
import jp.eijenson.connpass_searcher.infra.repository.firebase.RemoteConfigRepository
import jp.eijenson.connpass_searcher.util.d
import jp.eijenson.connpass_searcher.view.content.JobServiceContent
import jp.eijenson.connpass_searcher.view.content.MainContent
import jp.eijenson.connpass_searcher.view.data.mapping.toViewEventList
import jp.eijenson.connpass_searcher.view.presenter.NotificationPresenter
import jp.eijenson.connpass_searcher.view.ui.adapter.EventListAdapter
import jp.eijenson.connpass_searcher.view.ui.adapter.SearchHistoryAdapter
import jp.eijenson.connpass_searcher.view.ui.container.EventList
import jp.eijenson.connpass_searcher.view.ui.container.EventListPage
import jp.eijenson.connpass_searcher.view.ui.fragment.PrefsFragment
import jp.eijenson.connpass_searcher.view.ui.service.FirstRunJobService
import jp.eijenson.model.Event
import jp.eijenson.model.SearchHistory
import jp.eijenson.model.list.FavoriteList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.page_develop.view.*
import kotlinx.android.synthetic.main.page_event_list.view.*
import kotlinx.android.synthetic.main.page_favorite_list.view.*
import kotlinx.android.synthetic.main.page_search_history.view.*
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class MainActivity : AppCompatActivity(),
        MainContent.View,
        EventList.Listener,
        JobServiceContent,
        KoinComponent {

    val presenter: MainContent.Presenter  by inject { parametersOf(this) }
    private lateinit var eventListPage: EventListPage

    private val remoteConfigRepository = RemoteConfigRepository()

    companion object {
        private const val KEY_KEYWORD = "keyword"
        private const val KEY_SELECTED_ITEM_ID = "selected_item_id"

        fun createIntent(context: Context, keyword: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(KEY_KEYWORD, keyword)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tool_bar)
        eventListPage = EventListPage(context = this, listener = this)

        if (!BuildConfig.DEBUG) {
            bottom_navigation.menu.removeItem(R.id.dev)
        }
        this.d("onCreate")
        presenter.onCreate()
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            if (bottom_navigation.selectedItemId == item.itemId) {
                return@setOnNavigationItemSelectedListener true
            }
            movePage(item.itemId)
            true
        }

        if (savedInstanceState == null) {
            setupPage()
            val keyword: String? = intent.getStringExtra(KEY_KEYWORD)
            if (!keyword.isNullOrEmpty()) {
                setKeyword(keyword)
                presenter.search(keyword)
            }
        } else {
            val selectedItemId = savedInstanceState.get(KEY_SELECTED_ITEM_ID) as Int
            movePage(selectedItemId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putInt(KEY_SELECTED_ITEM_ID, bottom_navigation.selectedItemId)
    }

    private fun movePage(itemId: Int) {
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().remove(it).commitNow()
        }
        when (itemId) {
            R.id.list -> {
                viewSearchView()
            }
            R.id.search -> {
                page?.removeAllViews()
                layoutInflater.inflate(R.layout.page_search_history, page)
                presenter.viewSearchHistoryPage()
            }
            R.id.favorite -> {
                page?.removeAllViews()
                layoutInflater.inflate(R.layout.page_favorite_list, page)
                presenter.viewFavoritePage()
            }
            R.id.setting -> {
                page?.removeAllViews()
                supportFragmentManager.beginTransaction().add(page.id, PrefsFragment()).commit()
            }
            R.id.dev -> {
                page?.removeAllViews()
                layoutInflater.inflate(R.layout.page_develop, page)
                presenter.viewDevelopPage()
            }
        }

    }

    override fun showSearchResult(eventList: List<Event>, available: Int) {
        if (page.tv_search_result_avaliable == null) return
        page.tv_search_result_avaliable.text = getString(R.string.search_result_available, available)
        val adapter = object : EventListAdapter(this@MainActivity,
                eventList
                        .toViewEventList(AddressGeoCoderRepository(this))
                        .toMutableList()) {
            override fun onFavoriteChange(favorite: Boolean, itemId: Long) {
                presenter.changedFavorite(favorite, itemId)
            }
        }

        val listResult = page?.list_result
        listResult?.adapter = adapter
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
            this,
            androidx.recyclerview.widget.LinearLayoutManager(this).orientation
        )
        listResult?.addItemDecoration(dividerItemDecoration)

        eventListPage.resetState()
    }

    override fun showReadMore(eventList: List<Event>) {
        if (page.list_result == null) return
        eventListPage.resetState()
        val adapter = page?.list_result?.adapter as EventListAdapter?
        adapter?.addItem(eventList.toViewEventList(AddressGeoCoderRepository(this)))
    }

    override fun showSearchErrorToast() {
        Toast.makeText(this, "通信失敗", Toast.LENGTH_SHORT).show()
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    override fun actionDone(text: String) {
        //TODO Test
        presenter.search(text)
    }

    override fun onClickSave(searchHistoryId: Long) {
        presenter.onClickSave(searchHistoryId)
    }

    override fun showDevText(text: String) {
        page?.tv_dev_1?.text = text
        page?.btn_dev_delete?.setOnClickListener {
            presenter.onClickDataDelete()
        }
        page?.btn_dev_switch_api?.setOnClickListener {
            presenter.onClickDevSwitchApi()
        }
        page?.btn_dev_notification?.setOnClickListener {
            NotificationPresenter(this).notifyNewArrival(3857, "テスト", 999)
            NotificationPresenter(this).notifyNewArrival(4324, "メルカリ", 431)
            //val table = (application as App).searchHistoryTable
            //val presenter = MyJobServicePresenter(
            //        this,
            //        SearchHistoryBoxRepository(table))

            //presenter.onStartJob()

        }

        page?.btn_dev_remote_config?.setOnClickListener {
            remoteConfigRepository.fetch()
            Toast.makeText(
                    this,
                    remoteConfigRepository.getWelcomeMessage(),
                    Toast.LENGTH_LONG)
                    .show()
        }
    }

    override fun showFavoriteList(favoriteList: FavoriteList) {
        val adapter = object : EventListAdapter(this@MainActivity, favoriteList.toViewEventList().toMutableList()) {
            override fun onFavoriteChange(favorite: Boolean, itemId: Long) {
                presenter.changedFavorite(favorite, itemId)
            }
        }

        val listFavorite = page?.list_favorite
        listFavorite?.adapter = adapter
        listFavorite?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
            this,
            androidx.recyclerview.widget.LinearLayoutManager(this).orientation
        )
        listFavorite?.addItemDecoration(dividerItemDecoration)
    }

    override fun showSearchHistoryList(searchHistoryList: List<SearchHistory>) {
        val adapter = object : SearchHistoryAdapter(this, searchHistoryList.toMutableList()) {
            override fun onSelectedListener(searchHistory: SearchHistory) {
                presenter.selectedSearchHistory(searchHistory)
            }

            override fun onClickDeleteListener(searchHistory: SearchHistory) {
                presenter.onClickDelete(searchHistory)
            }
        }
        val listSearchResult = page?.list_search_history
        listSearchResult?.adapter = adapter
        if (adapter.itemCount == 0) {
            page?.tv_not_search_history?.visibility = View.VISIBLE
        }
        listSearchResult?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
            this,
            androidx.recyclerview.widget.LinearLayoutManager(this).orientation
        )
        listSearchResult?.addItemDecoration(dividerItemDecoration)
    }

    override fun moveToSearchView() {
        viewSearchView()
        bottom_navigation.selectedItemId = R.id.list
    }

    override fun visibleSaveButton(searchHistoryId: Long) {
        page?.btn_save?.visibility = View.VISIBLE
        eventListPage.setSearchHistoryId(searchHistoryId)
    }

    override fun goneSaveButton() {
        page?.btn_save?.visibility = View.GONE
    }

    override fun visibleProgressBar() {
        eventListPage.visibleProgressBar()
    }

    override fun goneProgressBar() {
        eventListPage.goneProgressBar()
    }

    private fun viewSearchView() {
        page?.removeAllViews()
        setupPage()
    }

    override fun setKeyword(keyword: String) {
        page?.ed_search?.setText(keyword)
    }

    private fun setupPage() {
        page?.addView(eventListPage, ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ))
    }

    override fun onLoadMore(totalItemCount: Int) {
        presenter.readMoreSearch(totalItemCount)
        Toast.makeText(this, "onLoadMore$totalItemCount", Toast.LENGTH_SHORT).show()
    }

    override fun showNotification(id: Int, keyword: String, count: Int) {
        NotificationPresenter(applicationContext).notifyNewArrival(id, keyword, count)
    }

    override fun log(text: String) {
        this.d(text)
    }

    override fun startJob() {
        FirstRunJobService.schedule(this)
    }

}

