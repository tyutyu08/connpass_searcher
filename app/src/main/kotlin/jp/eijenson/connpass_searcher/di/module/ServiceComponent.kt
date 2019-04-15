package jp.eijenson.connpass_searcher.di.module

import dagger.Subcomponent
import jp.eijenson.connpass_searcher.view.ui.service.MyJobService

@Subcomponent(
    modules = [
        ServiceModule::class,
        PresenterModule::class
    ]
)
interface ServiceComponent {
    fun inject(service: MyJobService)
}