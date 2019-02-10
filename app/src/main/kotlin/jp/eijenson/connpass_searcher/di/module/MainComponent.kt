package jp.eijenson.connpass_searcher.di.module

import dagger.Subcomponent
import jp.eijenson.connpass_searcher.view.ui.activity.MainActivity
import javax.inject.Singleton

@Subcomponent(
    modules = [
        MainViewModule::class,
        PresenterModule::class
    ]
)
interface MainComponent {
    fun inject(activity: MainActivity)
}