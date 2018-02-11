package eijenson.connpass_searcher.ui.view.activity

import android.app.Activity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.Toast
import eijenson.connpass_searcher.R
import eijenson.connpass_searcher.repository.EventRepository
import eijenson.connpass_searcher.repository.api.EventRepositoryImpl
import eijenson.connpass_searcher.repository.entity.RequestEvent
import eijenson.connpass_searcher.ui.view.adapter.EventListAdapter
import eijenson.connpass_searcher.ui.view.container.EventList
import eijenson.connpass_searcher.ui.view.container.EventListView
import eijenson.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_event_list.*
import timber.log.Timber

class MainActivity : Activity(), MainContent.View, EventList.Listener {
    private lateinit var presenter: MainContent.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(tool_bar)
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            if (bottom_navigation.selectedItemId == item.itemId) {
                return@setOnNavigationItemSelectedListener true
            }
            when (item.itemId) {
                R.id.list -> {
                    val v: ConstraintLayout = page as ConstraintLayout
                    v.removeAllViews()
                    val v2 = EventListView(this)
                    v.addView(v2)
                    v2.listener = this
                }
                R.id.search -> {
                    val v: ConstraintLayout = page as ConstraintLayout
                    v.removeAllViews()
                    layoutInflater.inflate(R.layout.view_search_history, v)
                }
                R.id.favorite -> {

                }
            }
            true
        }

//        presenter = MainPresenter(this, EventRepositoryCache(this))
        presenter = MainPresenter(this, EventRepositoryImpl())
        presenter.search()
    }

    override fun showSearchResult(eventList: List<Event>, available: Int) {
        tv_search_result_avaliable.text = getString(R.string.search_result_available, available)
        list_result.adapter = EventListAdapter(this, eventList)
    }

    override fun showSearchErrorToast() {
        Toast.makeText(this, "通信失敗", Toast.LENGTH_SHORT).show()
    }

    override fun actionDone(text: String) {
        presenter.search(text)
    }
}

interface MainContent {
    interface View {
        fun showSearchResult(eventList: List<Event>, available: Int)
        fun showSearchErrorToast()

    }

    interface Presenter {
        fun search(keyword: String = "")

    }
}

class MainPresenter(private val view: MainContent.View, private val repository: EventRepository) : MainContent.Presenter {

    override fun search(keyword: String) {
        Timber.d("keyword=%s", keyword)
        repository.getEvent(RequestEvent(keyword = keyword))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeBy(
                        onNext = {
                            view.showSearchResult(it.events, it.resultsAvailable)
                        },
                        onError = {
                            Timber.d(it)
                            view.showSearchErrorToast()
                        }
                )
    }
}