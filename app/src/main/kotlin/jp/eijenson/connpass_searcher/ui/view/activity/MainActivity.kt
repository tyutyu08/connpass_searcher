package jp.eijenson.connpass_searcher.ui.view.activity

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.BuildConfig
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.content.JobServiceContent
import jp.eijenson.connpass_searcher.content.MainContent
import jp.eijenson.connpass_searcher.presenter.MainPresenter
import jp.eijenson.connpass_searcher.presenter.MyJobServicePresenter
import jp.eijenson.connpass_searcher.presenter.NotificationPresenter
import jp.eijenson.connpass_searcher.repository.api.EventRepositoryImpl
import jp.eijenson.connpass_searcher.repository.db.AddressLocalRepository
import jp.eijenson.connpass_searcher.repository.db.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.repository.db.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.repository.file.EventRepositoryFile
import jp.eijenson.connpass_searcher.repository.firebase.RemoteConfigRepository
import jp.eijenson.connpass_searcher.repository.local.DevLocalRepository
import jp.eijenson.connpass_searcher.repository.local.SettingsLocalRepository
import jp.eijenson.connpass_searcher.ui.service.FirstRunJobService
import jp.eijenson.connpass_searcher.ui.view.adapter.EventListAdapter
import jp.eijenson.connpass_searcher.ui.view.adapter.SearchHistoryAdapter
import jp.eijenson.connpass_searcher.ui.view.container.EventList
import jp.eijenson.connpass_searcher.ui.view.container.EventListPage
import jp.eijenson.connpass_searcher.ui.view.data.mapping.toViewEventList
import jp.eijenson.connpass_searcher.ui.view.fragment.PrefsFragment
import jp.eijenson.connpass_searcher.util.d
import jp.eijenson.model.Event
import jp.eijenson.model.SearchHistory
import jp.eijenson.model.list.FavoriteList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.page_develop.view.*
import kotlinx.android.synthetic.main.page_event_list.view.*
import kotlinx.android.synthetic.main.page_favorite_list.view.*
import kotlinx.android.synthetic.main.page_search_history.view.*
import timber.log.Timber


class MainActivity : AppCompatActivity(), MainContent.View, EventList.Listener, JobServiceContent, PrefsFragment.Listener {

    private lateinit var presenter: MainContent.Presenter
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

        // ローカル向き = EventRepositoryFile
        // API向き = EventRepositoryImpl
        if (BuildConfig.DEBUG) {
            refreshPresenter(false)
        } else {
            refreshPresenter(true)
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
                setKeyword(keyword!!)
                presenter.search(keyword)
            }
        } else {
            val selectedItemId = savedInstanceState.get(KEY_SELECTED_ITEM_ID) as Int
            movePage(selectedItemId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
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
        page?.tv_search_result_avaliable?.text = getString(R.string.search_result_available, available)
        val adapter = object : EventListAdapter(this@MainActivity,
                eventList
                        .toViewEventList(AddressLocalRepository(this))
                        .toMutableList()) {
            override fun onFavoriteChange(favorite: Boolean, itemId: Long) {
                presenter.changedFavorite(favorite, itemId)
            }
        }

        val listResult = page?.list_result
        listResult?.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(this,
                LinearLayoutManager(this).orientation)
        listResult?.addItemDecoration(dividerItemDecoration)

        eventListPage.resetState()
    }

    override fun showReadMore(eventList: List<Event>) {
        eventListPage.resetState()
        val adapter = page?.list_result?.adapter as EventListAdapter
        adapter.addItem(eventList.toViewEventList(AddressLocalRepository(this)))
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
            val table = (application as App).searchHistoryTable
            val presenter = MyJobServicePresenter(
                    this,
                    SearchHistoryLocalRepository(table))

            presenter.onStartJob()

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
        listFavorite?.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this,
                LinearLayoutManager(this).orientation)
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
        listSearchResult?.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this,
                LinearLayoutManager(this).orientation)
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

    override fun refreshPresenter(isApi: Boolean) {
        val eventRepository =
                if (isApi) {
                    EventRepositoryImpl()
                } else {
                    EventRepositoryFile(this)
                }
        presenter = MainPresenter(this,
                eventRepository,
                FavoriteLocalRepository((application as App).favoriteTable),
                SearchHistoryLocalRepository((application as App).searchHistoryTable),
                DevLocalRepository(this),
                SettingsLocalRepository(this))
    }

    override fun showNotification(id: Int, keyword: String, count: Int) {
        NotificationPresenter(applicationContext).notifyNewArrival(id, keyword, count)
    }

    override fun onChangedNotification(isEnable: Boolean) {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if (isEnable) {
            startJob()
        } else {
            scheduler.cancelAll()
        }
        Timber.d(scheduler.allPendingJobs.count().toString())
        scheduler.allPendingJobs.forEach { Timber.d(it.toString()) }
    }

    override fun log(text: String) {
        this.d(text)
    }

    override fun startJob() {
        val componentName = ComponentName(this, FirstRunJobService::class.java)

        // デバッグ時は15分,リリース版は6時間ごと
        val periodic = if (BuildConfig.DEBUG) 15 * 60 * 1000L else 6 * 60 * 60 * 1000L
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1, componentName)
                .setMinimumLatency(periodic)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()

        scheduler.schedule(jobInfo)
    }

}

