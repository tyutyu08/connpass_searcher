package jp.eijenson.connpass_searcher.analytics

import java.util.Locale

enum class Event {
    JOB_START;

    override fun toString(): String {
        return this.name.toLowerCase(Locale.getDefault())
    }
}
