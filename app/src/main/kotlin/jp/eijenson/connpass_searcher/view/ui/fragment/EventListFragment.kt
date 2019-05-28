package jp.eijenson.connpass_searcher.view.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxkotlin.subscribeBy
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.di.module.ViewModelModule
import jp.eijenson.connpass_searcher.domain.usecase.SearchUseCase
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import jp.eijenson.connpass_searcher.view.data.ViewEvent
import jp.eijenson.connpass_searcher.view.data.mapping.toViewEventList
import jp.eijenson.connpass_searcher.view.ui.adapter.EventListAdapter
import jp.eijenson.connpass_searcher.view.ui.listener.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.page_event_list.*
import kotlinx.android.synthetic.main.page_event_list.view.*
import timber.log.Timber
import javax.inject.Inject

class EventListFragment() : EventList.View, Fragment() {

    private var searchHistoryId: Long = -1
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    lateinit var eventListAdapter: EventListAdapter

    @Inject
    lateinit var viewModel: EventListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.page_event_list, container, false)

        App.app.appComponent.plus(ViewModelModule(this))
            .inject(this)

        viewModel.onCreate()

        viewModel.eventList.observe(this, Observer {
            eventListAdapter.addItem(it)
        })

        viewModel.loading.observe(this, Observer {
            progress_bar.isVisible = it
        })


        view.search.isSubmitButtonEnabled = true
        view.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.d("onQueryTextSubmit")
                viewModel.searchWord.value = query
                viewModel.onSubmit()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchWord.value = newText
                return true
            }
        })

        view.search.setOnSearchClickListener {
            viewModel.searchWord.value = view.search.query.toString()
            viewModel.onSubmit()
        }
        view.btn_save.setOnClickListener {
            if (searchHistoryId == -1L) return@setOnClickListener
            viewModel.onClickSave.value = searchHistoryId
        }

        eventListAdapter = object : EventListAdapter(this.requireContext(), mutableListOf()) {
            override fun onFavoriteChange(favorite: Boolean, item: ViewEvent) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        view.list_result.adapter = eventListAdapter

        view.list_result.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        scrollListener = object :
            EndlessRecyclerViewScrollListener(view.list_result.layoutManager as androidx.recyclerview.widget.LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView) {
                viewModel.onLoadMore.value = totalItemsCount
            }
        }
        view.list_result.addOnScrollListener(scrollListener)

        return view
    }

    override fun setSearchHistoryId(id: Long) {
        searchHistoryId = id
    }

    override fun resetState() {
        scrollListener.resetState()
    }
}

interface EventList {
    interface View {
        fun setSearchHistoryId(id: Long)
        fun resetState()
    }
}

class EventListViewModel(
    val searchUseCase: SearchUseCase
) : ViewModel() {
    class Factory(
        val searchUseCase: SearchUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EventListViewModel(searchUseCase) as T
        }
    }

    val searchWord = MutableLiveData<String>()

    val onClickSave = MutableLiveData<Long>()

    val onLoadMore = MutableLiveData<Int>()

    val eventList = MutableLiveData<List<ViewEvent>>()

    val loading = MutableLiveData<Boolean>()

    fun onCreate() {
    }

    @SuppressLint("CheckResult")
    fun onSubmit() {
        loading.value = true
        searchUseCase.search(RequestEvent(keyword = searchWord.value))
            .doFinally {
                loading.value = false
            }
            .subscribeBy(
                onError = {
                    Timber.d(it)
                },
                onSuccess = {
                    eventList.value = it.events.toViewEventList()
                }
            )
    }
}