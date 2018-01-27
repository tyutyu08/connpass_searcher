package eijenson.connpass_searcher.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import eijenson.connpass_searcher.R
import eijenson.connpass_searcher.ui.view.data.ItemEvent
import kotlinx.android.synthetic.main.item_event.view.*

class EventListAdapter(val context: Context, private val objects: List<ItemEvent>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.item_event, parent, false)

        val item = getItem(position)

        view.tv_title.text = item.title
        view.tv_date.text = item.startedAt.date
        view.tv_time.text = """${item.startedAt.time} ~ ${item.endedAt.time}"""
        view.tv_address.text = item.address
        view.tv_place.text = item.place
        view.tv_accept.text = item.viewAccept()

        if (item.isAccept()) {
            view.tv_accept.setTextColor(context.resources.getColor(R.color.colorAccent))
        } else {
            view.tv_accept.setTextColor(context.resources.getColor(R.color.colorPrimary))
        }
        return view
    }

    override fun getItem(position: Int): ItemEvent {
        return objects.get(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = objects.size
}