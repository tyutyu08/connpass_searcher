package jp.eijenson.connpass_searcher.view.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.eijenson.connpass_searcher.R
import jp.eijenson.model.SearchHistory
import kotlinx.android.synthetic.main.item_search_history.view.*

/**
 * Created by kobayashimakoto on 2018/03/12.
 */
abstract class SearchHistoryAdapter(context: Context,
                                    private val objects: MutableList<SearchHistory>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryHolder {
        val view = layoutInflater.inflate(R.layout.item_search_history, parent, false)
        return SearchHistoryHolder(view)
    }

    override fun getItemCount(): Int = objects.size

    override fun onBindViewHolder(holder: SearchHistoryHolder, position: Int) {
        val item = objects[position]

        holder.itemView.tv_keyword.text = item.keyword
        holder.itemView.tv_prefecture.text = if (item.prefecture == "") "全国" else item.prefecture

        holder.itemView.setOnClickListener {
            onSelectedListener(item)
        }

        holder.itemView.btn_delete.setOnClickListener {
            onClickDeleteListener(item)
            objects.remove(item)
            notifyDataSetChanged()
        }
    }

    abstract fun onSelectedListener(searchHistory: SearchHistory)

    abstract fun onClickDeleteListener(searchHistory: SearchHistory)

    class SearchHistoryHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}