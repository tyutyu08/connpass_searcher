package eijenson.connpass_searcher.ui.view.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import eijenson.connpass_searcher.R
import eijenson.connpass_searcher.repository.EventRepository
import eijenson.connpass_searcher.repository.api.EventRepositoryImpl
import eijenson.connpass_searcher.repository.entity.RequestEvent
import eijenson.connpass_searcher.ui.view.adapter.EventListAdapter
import eijenson.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_event_list.*

class MainActivity : Activity(), MainContent.View {
    private lateinit var presenter: MainContent.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(tool_bar)
        ed_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.search(ed_search.text.toString())
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
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
        Log.d("MainPresenter", "keyword=" + keyword)
        repository.getEvent(RequestEvent(keyword = keyword))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeBy(
                        onNext = {
                            view.showSearchResult(it.events, it.resultsAvailable)
                        },
                        onError = {
                            Log.d("MainActivity", "search", it)
                            view.showSearchErrorToast()
                        }
                )
    }
}