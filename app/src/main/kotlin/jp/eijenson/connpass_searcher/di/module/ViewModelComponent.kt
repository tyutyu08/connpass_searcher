package jp.eijenson.connpass_searcher.di.module

import dagger.Subcomponent
import jp.eijenson.connpass_searcher.view.ui.fragment.DevFragment
import jp.eijenson.connpass_searcher.view.ui.fragment.EventListFragment
import jp.eijenson.connpass_searcher.view.ui.fragment.FavoriteFragment

@Subcomponent(
    modules = [
        ViewModelModule::class
    ]
)
interface ViewModelComponent {
    fun inject(fragment: FavoriteFragment)
    fun inject(fragment: DevFragment)
    fun inject(fragment: EventListFragment)
}