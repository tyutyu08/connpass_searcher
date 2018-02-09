package eijenson.connpass_searcher.ui.view.activity

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.google.gson.Gson
import eijenson.connpass_searcher.R
import eijenson.connpass_searcher.model.EventModel
import eijenson.connpass_searcher.repository.api.response.ResultEvent
import eijenson.connpass_searcher.ui.view.adapter.EventListAdapter
import eijenson.connpass_searcher.ui.view.data.ItemEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(tool_bar)
        ed_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            }
            false
        }
        val inputStream = assets.open("result.json")
        val list = inputStream.reader().use { Gson().fromJson(it, ResultEvent::class.java).events }
        val list2 = ArrayList<ItemEvent>()
        list?.forEach {
            list2.add(EventModel(it).convertItemEvent())
        }
        list_result.adapter = EventListAdapter(this, list2)
    }
}