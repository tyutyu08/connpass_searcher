package jp.eijenson.connpass_searcher.ui.view.adapter

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.ui.view.data.ViewDate
import jp.eijenson.model.Event
import kotlinx.android.synthetic.main.item_event.view.*

class EventListAdapter(val context: Context, private val objects: List<Event>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.item_event, parent, false)

        val item = getItem(position)

        view.tv_title.text = item.title
        view.tv_date.text = ViewDate(item.startedAt).date
        view.tv_time.text = "${ViewDate(item.startedAt).time} ~ ${ViewDate(item.endedAt).time}"
        view.tv_accept.text = viewAccept(item)
        view.tv_address.text = item.prefecture.let { it.prefectureName + it.prefix }
        view.tv_series_title.text = item.series.title

        view.setOnClickListener {
            val tabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
            tabsIntent.launchUrl(context, Uri.parse(item.eventUrl))
        }

        view.favorite.setOnFavoriteChangeListener { _, favorite ->
            if (favorite) {
                Toast.makeText(context, "add Favorite ${item.title}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "remove Favorite ${item.title}", Toast.LENGTH_SHORT).show()
            }
        }

        if (isAccept(item)) {
            view.tv_accept.setTextColor(context.resources.getColor(R.color.colorAccent))
        } else {
            view.tv_accept.setTextColor(context.resources.getColor(R.color.colorPrimary))
        }
        return view
    }

    override fun getItem(position: Int): Event {
        return objects.get(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = objects.size

    // TODO:ここじゃない
    fun viewAccept(event: Event): String {
        return if (isAccept(event)) "参加可能" else """${event.waiting}人キャンセル待ち"""
    }

    // TODO:ここじゃない
    fun isAccept(event: Event): Boolean {
        return event.accepted <= event.limit
    }

}