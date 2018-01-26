package eijenson.connpass_searcher.ui.view.activity

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import eijenson.connpass_searcher.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by kobayashimakoto on 2018/01/26.
 */
class MainActivity() : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(tool_bar)
        ed_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

            }
            false
        }
    }
}