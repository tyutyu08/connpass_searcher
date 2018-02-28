package jp.eijenson.connpass_searcher.ui.view.adapter

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.ui.view.data.ViewDate
import jp.eijenson.model.Event
import kotlinx.android.synthetic.main.item_event.view.*
import timber.log.Timber

abstract class EventListAdapter(internal val context: Context,
                                internal val objects: List<Event>) : RecyclerView.Adapter<EventListAdapter.EventItemHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventItemHolder {
        val view = layoutInflater.inflate(R.layout.item_event, parent, false)
        return EventItemHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    override fun onBindViewHolder(holder: EventItemHolder, position: Int) {

        val item = objects.get(position)

        Timber.d("onBindViewHolder holder:$holder item.eventId:${item.eventId}")

        holder.itemView.tv_title.text = item.title
        holder.itemView.tv_date.text = ViewDate(item.startedAt).date
        holder.itemView.tv_time.text = "${ViewDate(item.startedAt).time} ~ ${ViewDate(item.endedAt).time}"
        holder.itemView.tv_accept.text = viewAccept(item)
        holder.itemView.tv_address.text = item.prefecture.let { it.prefectureName + it.prefix }
        holder.itemView.tv_series_title.text = item.series.title

        holder.itemView.setOnClickListener {
            val tabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
            tabsIntent.launchUrl(context, Uri.parse(item.eventUrl))
        }

        holder.itemView.favorite.setFavorite(item.isFavorite, false)
        holder.itemView.favorite.setOnClickListener {
            Timber.d("onBindViewHolder holder:$holder item.eventId:${item.eventId}")
            holder.itemView.favorite.toggleFavorite()
            item.isFavorite = holder.itemView.favorite.isFavorite
            onFavoriteChange(item.isFavorite, item)
        }

        if (isAccept(item)) {
            holder.itemView.tv_accept.setTextColor(context.resources.getColor(R.color.colorAccent))
        } else {
            holder.itemView.tv_accept.setTextColor(context.resources.getColor(R.color.colorPrimary))
        }
    }

    abstract fun onFavoriteChange(favorite: Boolean, item: Event)


    class EventItemHolder(view: View) : RecyclerView.ViewHolder(view)

    // TODO:ここじゃない
    fun viewAccept(event: Event): String {
        return if (isAccept(event)) "参加可能" else """${event.waiting}人キャンセル待ち"""
    }

    // TODO:ここじゃない
    fun isAccept(event: Event): Boolean {
        return event.accepted <= event.limit
    }

}