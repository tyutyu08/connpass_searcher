package eijenson.model

data class ResultEvent(
        val resultsReturned: Int,
        val resultsAvailable: Int,
        val resultsStart: Int,
        val events: List<Event>
)