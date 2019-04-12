package jp.eijenson.connpass_searcher.di.module

import dagger.Component
import jp.eijenson.connpass_searcher.App
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)
    fun plus(viewModule: MainViewModule, presenterModule: PresenterModule): MainComponent
    fun plus(viewModelModule: ViewModelModule): DevComponent
}