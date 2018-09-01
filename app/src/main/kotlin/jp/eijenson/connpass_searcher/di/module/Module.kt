package jp.eijenson.connpass_searcher.di.module

import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import jp.eijenson.connpass_searcher.domain.repository.*
import jp.eijenson.connpass_searcher.domain.usecase.SearchUseCase
import jp.eijenson.connpass_searcher.infra.repository.api.EventApiRepository
import jp.eijenson.connpass_searcher.infra.repository.db.FavoriteBoxRepository
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryBoxRepository
import jp.eijenson.connpass_searcher.infra.repository.db.entity.MyObjectBox
import jp.eijenson.connpass_searcher.infra.repository.local.DevSharedRepository
import jp.eijenson.connpass_searcher.infra.repository.local.SettingsSharedRepository
import jp.eijenson.connpass_searcher.view.content.JobServiceContent
import jp.eijenson.connpass_searcher.view.content.MainContent
import jp.eijenson.connpass_searcher.view.presenter.MainPresenter
import jp.eijenson.connpass_searcher.view.presenter.MyJobServicePresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

// TODO:ローカル向き = EventFileRepository
// TODO:API向き = EventApiRepository


val myModule: Module = module {
    factory<MainContent.Presenter> { (view: MainContent.View) -> MainPresenter(view, get(), get(), get(), get()) }
    factory<MyJobServicePresenter> { (view: JobServiceContent) -> MyJobServicePresenter(view) }
    factory<SearchUseCase> { SearchUseCase(get(), get()) }
    single<EventRemoteRepository> { EventApiRepository() }
    single<BoxStore> { MyObjectBox.builder().androidContext(androidContext()).build() }
    single<FavoriteLocalRepository> {
        FavoriteBoxRepository((get() as BoxStore).boxFor(), (get() as BoxStore).boxFor())
    }
    single<SearchHistoryLocalRepository> {
        SearchHistoryBoxRepository((get() as BoxStore).boxFor())
    }
    single<DevLocalRepository> { DevSharedRepository(androidContext()) }
    single<SettingsLocalRepository> { SettingsSharedRepository(androidContext()) }
}
