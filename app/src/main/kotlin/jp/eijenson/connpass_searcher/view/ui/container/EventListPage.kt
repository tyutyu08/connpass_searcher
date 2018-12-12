package jp.eijenson.connpass_searcher.view.ui.container

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.view.ui.listener.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.page_event_list.view.*

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
    private val scrollListener: EndlessRecyclerViewScrollListener

    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0,
                listener: EventList.Listener) : this(context, attrs, defStyleAttr) {
        this.listener = listener
    }


    init {
        LayoutInflater.from(context).inflate(R.layout.page_event_list, this)

        search.setOnClickListener {
            listener.actionDone(ed_search.text.toString())
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        ed_search.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listener.actionDone(ed_search.text.toString())
                val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
        }

        btn_save.setOnClickListener {
            if (searchHistoryId == -1L) return@setOnClickListener
            listener.onClickSave(searchHistoryId)
        }
        list_result.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        scrollListener = object : EndlessRecyclerViewScrollListener(list_result.layoutManager as androidx.recyclerview.widget.LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView) {
                listener.onLoadMore(totalItemsCount)
            }
        }
        list_result.addOnScrollListener(scrollListener)
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