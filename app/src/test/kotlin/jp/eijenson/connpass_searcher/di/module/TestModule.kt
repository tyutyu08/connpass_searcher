package jp.eijenson.connpass_searcher.di.module

import jp.eijenson.connpass_searcher.domain.repository.EventRemoteRepository
import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.domain.usecase.SearchUseCase
import jp.eijenson.connpass_searcher.infra.repository.api.EventTestRepository
import jp.eijenson.connpass_searcher.infra.repository.db.SearchHistoryDebugRepository
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val testModule: Module = module {
    factory<SearchUseCase> { SearchUseCase(get(), get()) }
    single<EventRemoteRepository> { EventTestRepository() }
    single<SearchHistoryLocalRepository> { SearchHistoryDebugRepository() }
}
