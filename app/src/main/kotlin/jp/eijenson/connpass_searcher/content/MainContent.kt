package jp.eijenson.connpass_searcher.content

import jp.eijenson.model.Event
import jp.eijenson.model.SearchHistory
import jp.eijenson.model.list.FavoriteList

/**
 * Created by makoto.kobayashi on 2018/04/16.
 */
interface MainContent {
    interface View {
        fun showSearchResult(eventList: List<Event>, available: Int)
        fun showSearchErrorToast()
        fun showToast(text: String)
        fun showDevText(text: String)
        fun showFavoriteList(favoriteList: FavoriteList)
        fun showSearchHistoryList(searchHistoryList: List<SearchHistory>)
        fun visibleSaveButton(searchHistoryId: Long)
        fun setKeyword(keyword: String)
        fun goneSaveButton()
        fun moveToSearchView()
        fun finish()
        fun visibleProgressBar()
        fun goneProgressBar()
        fun refreshPresenter(isApi: Boolean)
        fun showReadMore(eventList: List<Event>)
    }

    interface Presenter {

        fun changedFavorite(favorite: Boolean, itemId: Long)

        // TODO:したの開発ボタン押されたよ
        fun viewDevelopPage()

        // TODO:したのお気に入りボタン押されたよ
        fun viewFavoritePage()

        // TODO:したの検索履歴ボタン押されたよ
        fun viewSearchHistoryPage()

        fun selectedSearchHistory(searchHistory: SearchHistory)

        fun onClickDataDelete()

        fun onClickSave(searchHistoryId: Long)

        fun onClickDelete(searchHistory: SearchHistory)

        fun onClickDevSwitchApi()

        // domain層
        fun search(keyword: String = "", start: Int = 0)

        fun readMoreSearch(start: Int = 0)
    }
}