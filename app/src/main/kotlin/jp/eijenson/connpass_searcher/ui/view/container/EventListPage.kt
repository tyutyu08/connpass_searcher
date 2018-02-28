package jp.eijenson.connpass_searcher.ui.view.container

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import jp.eijenson.connpass_searcher.R
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

    private lateinit var listener: EventList.Listener

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
        list_result.layoutManager = LinearLayoutManager(context)
    }
}

interface EventList {
    interface View

    interface Listener {
        fun actionDone(text: String)
    }

}