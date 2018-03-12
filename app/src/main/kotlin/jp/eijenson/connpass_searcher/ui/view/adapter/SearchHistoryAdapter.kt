package jp.eijenson.connpass_searcher.ui.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.repository.entity.RequestEvent
import kotlinx.android.synthetic.main.item_search_history.view.*

/**
 * Created by kobayashimakoto on 2018/03/12.
 */
abstract class SearchHistoryAdapter(context: Context,
                                    private val objects: List<RequestEvent>)
    : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchHistoryHolder {
        val view = layoutInflater.inflate(R.layout.item_search_history, parent, false)
        return SearchHistoryHolder(view)
    }

    override fun getItemCount(): Int = objects.size

    override fun onBindViewHolder(holder: SearchHistoryHolder?, position: Int) {
        if (holder == null) return
        val item = objects[position]

        holder.itemView.tv_keyword.text = item.keyword
        holder.itemView.tv_other.text = item.toString()

        holder.itemView.setOnClickListener {
            onSelectedListener(item)
        }
    }

    abstract fun onSelectedListener(requestEvent: RequestEvent)

    class SearchHistoryHolder(view: View) : RecyclerView.ViewHolder(view)
}