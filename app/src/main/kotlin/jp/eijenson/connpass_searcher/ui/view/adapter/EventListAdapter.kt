package jp.eijenson.connpass_searcher.ui.view.adapter

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.ui.view.data.ViewDate
import jp.eijenson.connpass_searcher.ui.view.data.ViewEvent
import kotlinx.android.synthetic.main.item_event.view.*
import timber.log.Timber

abstract class EventListAdapter(internal val context: Context,
                                internal val objects: MutableList<ViewEvent>) : RecyclerView.Adapter<EventListAdapter.EventItemHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemHolder {
        val view = layoutInflater.inflate(R.layout.item_event, parent, false)
        return EventItemHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    fun addItem(list: List<ViewEvent>) {
        objects.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventItemHolder, position: Int) {

        val item = objects.get(position)

        Timber.d("onBindViewHolder holder:$holder item.eventId:${item.eventId}")

        holder.itemView.tv_title.text = item.title
        holder.itemView.tv_date.text = ViewDate(item.startedAt).date
        holder.itemView.tv_time.text = "${ViewDate(item.startedAt).time} ~ ${ViewDate(item.endedAt).time}"
        holder.itemView.tv_accept.text = "${item.accepted} / ${item.limit} ${item.viewAccept()}"
        holder.itemView.tv_address.text = if (item.address.isEmpty()) {
            item.prefecture.let { it.prefectureName + it.prefix }
        } else {
            item.address
        }

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
            onFavoriteChange(item.isFavorite, item.eventId)
        }

        if (item.isAccept()) {
            holder.itemView.tv_accept.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        } else {
            holder.itemView.tv_accept.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }

    abstract fun onFavoriteChange(favorite: Boolean, itemId: Long)

    class EventItemHolder(view: View) : RecyclerView.ViewHolder(view)
}