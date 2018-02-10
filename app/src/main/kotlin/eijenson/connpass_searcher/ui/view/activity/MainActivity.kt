package eijenson.connpass_searcher.ui.view.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import eijenson.connpass_searcher.R
import eijenson.connpass_searcher.repository.EventRepository
import eijenson.connpass_searcher.repository.cache.EventRepositoryCache
import eijenson.connpass_searcher.repository.entity.RequestEvent
import eijenson.connpass_searcher.ui.view.adapter.EventListAdapter
import eijenson.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), MainContent.View {
    private lateinit var presenter: MainContent.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(tool_bar)
        ed_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            }
            false
        }

        presenter = MainPresenter(this, EventRepositoryCache(this))
        presenter.search()
    }

    override fun showSearchResult(eventList: List<Event>) {
        list_result.adapter = EventListAdapter(this, eventList)
    }

    override fun showSearchErrorToast() {
        Toast.makeText(this, "通信失敗", Toast.LENGTH_SHORT).show()
    }
}

interface MainContent {
    interface View {
        fun showSearchResult(eventList: List<Event>)
        fun showSearchErrorToast()

    }

    interface Presenter {
        fun search()

    }
}

class MainPresenter(private val view: MainContent.View, private val repository: EventRepository) : MainContent.Presenter {

    override fun search() {
        repository.getEvent(RequestEvent(keyword = "Android"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeBy(
                        onNext = {
                            view.showSearchResult(it.events)
                        },
                        onError = {
                            Log.d("MainActivity", "search", it)
                            view.showSearchErrorToast()
                        }
                )
    }
}