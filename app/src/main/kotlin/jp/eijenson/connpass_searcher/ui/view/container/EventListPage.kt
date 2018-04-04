package jp.eijenson.connpass_searcher.ui.view.container

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.ui.view.listener.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.page_event_list.view.*
import timber.log.Timber

/**
 * Created by kobayashimakoto on 2018/02/11.
 */
class EventListPage @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr), EventList.View {
    override fun visibleProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun goneProgressBar() {
        progress_bar.visibility = View.GONE
    }

    private lateinit var listener: EventList.Listener
    private var searchHistoryId: Long = -1

    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0,
                listener: EventList.Listener) : this(context, attrs, defStyleAttr) {
        this.listener = listener
    }


    init {
        LayoutInflater.from(context).inflate(R.layout.page_event_list, this)
        ed_search.setOnEditorActionListener { v, actionId, _ ->
            Timber.d("onEditorAction")
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Timber.d("IME_ACTION_DONE")
                listener.actionDone(ed_search.text.toString())
                val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
        }

        btn_save.setOnClickListener {
            if(searchHistoryId == -1L) return@setOnClickListener
            listener.onClickSave(searchHistoryId)
        }
        list_result.layoutManager = LinearLayoutManager(context)
        val listener = object: EndlessRecyclerViewScrollListener(LinearLayoutManager(context)){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                listener.onLoadMore(totalItemsCount)
            }
        }
        list_result.addOnScrollListener(listener)
        listener.resetState()
    }

    override fun setSearchHistoryId(id: Long) {
        searchHistoryId = id
    }
}

interface EventList {
    interface View {
        fun setSearchHistoryId(id: Long)
        fun visibleProgressBar()
        fun goneProgressBar()
    }

    interface Listener {
        fun actionDone(text: String)

        fun onClickSave(searchHistoryId: Long)

        fun onLoadMore(totalItemCount: Int)
    }

}