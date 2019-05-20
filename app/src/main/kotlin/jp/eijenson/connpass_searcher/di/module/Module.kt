package jp.eijenson.connpass_searcher.di.module

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.domain.repository.DevLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.EventRemoteRepository
import jp.eijenson.connpass_searcher.domain.repository.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.SettingsLocalRepository
import jp.eijenson.connpass_searcher.domain.usecase.SearchUseCase
import jp.eijenson.connpass_searcher.infra.repository.api.EventApiRepository
import jp.eijenson.connpass_searcher.infra.repository.db.BoxStoreProvider
import jp.eijenson.connpass_searcher.infra.repository.db.FavoriteBoxRepository
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryBoxRepository
import jp.eijenson.connpass_searcher.infra.repository.firebase.RemoteConfigRepository
import jp.eijenson.connpass_searcher.infra.repository.local.DevSharedRepository
import jp.eijenson.connpass_searcher.infra.repository.local.SettingsSharedRepository
import jp.eijenson.connpass_searcher.view.content.JobServiceContent
import jp.eijenson.connpass_searcher.view.content.MainContent
import jp.eijenson.connpass_searcher.view.presenter.MainPresenter
import jp.eijenson.connpass_searcher.view.presenter.MyJobServicePresenter
import jp.eijenson.connpass_searcher.view.ui.activity.MainActivity
import jp.eijenson.connpass_searcher.view.ui.fragment.DevViewModel
import jp.eijenson.connpass_searcher.view.ui.fragment.EventListViewModel
import jp.eijenson.connpass_searcher.view.ui.service.MyJobService
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {
    @Provides
    fun provideContext(): Context = app.applicationContext
}

@Module
class MainViewModule(private val activity: MainActivity) {
    @Provides
    fun provideMainView(): MainContent.View = activity
}

@Module
class ServiceModule(private val service: MyJobService) {
    @Provides
    fun provideMainView(): JobServiceContent.View = service
}

@Module
class PresenterModule {

    @Provides
    fun provideMainPresenter(
        view: MainContent.View,
        searchUseCase: SearchUseCase,
        favoriteLocalRepository: FavoriteLocalRepository,
        searchHistoryLocalRepository: SearchHistoryLocalRepository,
        devLocalRepository: DevLocalRepository,
        settingsLocalRepository: SettingsLocalRepository
    ): MainContent.Presenter = MainPresenter(
        view,
        searchUseCase,
        favoriteLocalRepository,
        searchHistoryLocalRepository,
        devLocalRepository,
        settingsLocalRepository
    )

    @Provides
    fun provideMyJobServicePresenter(
        view: JobServiceContent.View,
        searchUseCase: SearchUseCase
    ): JobServiceContent.Presenter = MyJobServicePresenter(view, searchUseCase)
}

@Module
class ViewModelModule(private val fragment: Fragment) {

    @Provides
    fun provideDevViewModel(
        factory: DevViewModel.Factory
    ) = ViewModelProviders.of(fragment, factory).get(DevViewModel::class.java)

    @Provides
    fun provideEventListModel(
        factory: EventListViewModel.Factory
    ) = ViewModelProviders.of(fragment, factory).get(EventListViewModel::class.java)
}

@Module
class ViewModelFactoryModule {
    @Provides
    fun provideDevViewModelFactory(
        devLocalRepository: DevLocalRepository,
        remoteConfigRepository: RemoteConfigRepository,
        searchHistoryLocalRepository: SearchHistoryLocalRepository,
        favoriteLocalRepository: FavoriteLocalRepository
    ) = DevViewModel.Factory(
        devLocalRepository,
        remoteConfigRepository,
        searchHistoryLocalRepository,
        favoriteLocalRepository
    )

    @Provides
    fun provideEventListModelFactory() = EventListViewModel.Factory()
}

@Module
class UseCaseModule {
    @Provides
    fun provideSearchUseCase(
        eventLocalRepository: EventRemoteRepository,
        searchHistoryLocalRepository: SearchHistoryLocalRepository
    ) = SearchUseCase(eventLocalRepository, searchHistoryLocalRepository)
}

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideEventRemoteRepository(): EventRemoteRepository = EventApiRepository()

    @Provides
    @Singleton
    fun provideBoxStore(context: Context) = BoxStoreProvider(context)

    @Provides
    @Singleton
    fun provideFavoriteRepository(boxStore: BoxStoreProvider): FavoriteLocalRepository =
        FavoriteBoxRepository(boxStore.provide())

    @Provides
    @Singleton
    fun provideSearchHistoryLocalRepository(boxStore: BoxStoreProvider): SearchHistoryLocalRepository =
        SearchHistoryBoxRepository(boxStore.provide())

    @Provides
    @Singleton
    fun provideDevLocalRepository(context: Context): DevLocalRepository = DevSharedRepository(context)

    @Provides
    @Singleton
    fun provideSettingsLocalRepository(context: Context): SettingsLocalRepository = SettingsSharedRepository(context)

    @Provides
    @Singleton
    fun remoteConfigRepository() = RemoteConfigRepository()
}