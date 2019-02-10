package jp.eijenson.connpass_searcher.domain.usecase

import jp.eijenson.connpass_searcher.di.module.testModule
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest

class SearchUseCaseTest : KoinTest {

    val useCase: SearchUseCase by inject()

    val requestEvent = RequestEvent()

    @Before
    fun before() {
        startKoin(listOf(testModule))
    }

    @Test
    fun search() {
        useCase.search(requestEvent)
                .test()
                .assertValueCount(1)
                .assertComplete()
    }

    @Test
    fun checkNewArrival() {
        useCase.checkNewArrival()
                .test()
                .assertComplete()
                .assertResult(Result(0, "", 0))
    }

    @After
    fun after() {
        stopKoin()
    }
}