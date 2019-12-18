package jp.eijenson.connpass_searcher.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.di.module.ViewModelModule
import jp.eijenson.connpass_searcher.view.ui.listener.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.page_event_list.*
import kotlinx.android.synthetic.main.page_event_list.view.*

class EventListFragment : EventList.View, Fragment() {

    lateinit var listener: EventList.Listener
    private var searchHistoryId: Long = -1
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.app.appComponent.plus(ViewModelModule(this))
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.page_event_list, container, false)

        view.search.setOnClickListener {
            listener.actionDone(ed_search.text.toString())
            val manager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        view.ed_search.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listener.actionDone(ed_search.text.toString())
                val manager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
        }

        view.btn_save.setOnClickListener {
            if (searchHistoryId == -1L) return@setOnClickListener
            listener.onClickSave(searchHistoryId)
        }
        view.list_result.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        scrollListener = object :
            EndlessRecyclerViewScrollListener(view.list_result.layoutManager as androidx.recyclerview.widget.LinearLayoutManager) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: androidx.recyclerview.widget.RecyclerView
            ) {
                listener.onLoadMore(totalItemsCount)
            }
        }
        view.list_result.addOnScrollListener(scrollListener)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EventList.Listener)
            listener = context
        else
            throw IllegalStateException("ActivityがEventListを継承していない")
    }

    override fun visibleProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun goneProgressBar() {
        progress_bar.visibility = View.GONE
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
        fun visibleProgressBar()
        fun goneProgressBar()
        fun resetState()
    }

    interface Listener {
        fun actionDone(text: String)

        fun onClickSave(searchHistoryId: Long)

        fun onLoadMore(totalItemCount: Int)
    }
}

class EventListViewModel : ViewModel() {
    class Factory : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EventListViewModel() as T
        }
    }
}