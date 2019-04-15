package jp.eijenson.connpass_searcher.domain.usecase

import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import org.junit.Test

class SearchUseCaseTest {

    val useCase = Any()

    val requestEvent = RequestEvent()

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
}