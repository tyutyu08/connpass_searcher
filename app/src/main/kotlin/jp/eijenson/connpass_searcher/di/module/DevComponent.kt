package jp.eijenson.connpass_searcher.di.module

import dagger.Subcomponent
import jp.eijenson.connpass_searcher.view.ui.fragment.DevFragment

@Subcomponent(
    modules = [
        ViewModelModule::class
    ]
)
interface DevComponent {
    fun inject(fragment: DevFragment)
}