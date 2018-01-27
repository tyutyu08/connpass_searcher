package eijenson.connpass_searcher.ui.view.data

data class ItemEvent(
        val title: String,
        val catchPhrase: String,
        val description: String,
        val eventUrl: String,
        val hashTag: String,
        val startedAt: ViewDate,
        val endedAt: ViewDate,
        val limit: Int,
        val eventType: String,
        val place: String,
        val accepted: Int,
        val waiting: Int
)