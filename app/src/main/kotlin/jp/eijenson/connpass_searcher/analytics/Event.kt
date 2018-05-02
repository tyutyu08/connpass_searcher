package jp.eijenson.connpass_searcher.analytics

import java.util.*

enum class Event {
    CLICK,
    JOB_START;

    override fun toString(): String {
        return this.name.toLowerCase(Locale.getDefault())
    }
}
