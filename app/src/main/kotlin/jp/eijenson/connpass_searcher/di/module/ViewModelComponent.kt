package jp.eijenson.connpass_searcher.di.module

import dagger.Subcomponent
import jp.eijenson.connpass_searcher.view.ui.fragment.DevFragment
import jp.eijenson.connpass_searcher.view.ui.fragment.EventListFragment

@Subcomponent(
    modules = [
        ViewModelModule::class
    ]
)
interface ViewModelComponent {
    fun inject(fragment: DevFragment)
    fun inject(fragment: EventListFragment)
}