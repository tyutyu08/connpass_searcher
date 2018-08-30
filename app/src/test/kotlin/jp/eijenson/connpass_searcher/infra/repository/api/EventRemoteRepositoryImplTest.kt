package jp.eijenson.connpass_searcher.infra.repository.api

import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Ignore
import org.junit.Test

class EventRemoteRepositoryImplTest {
    @Test
    @Ignore
    fun addition_isCorrect() {
        val repository = EventApiRepository()
        repository.getAll(RequestEvent(keyword = "Android")).subscribeBy(
                onNext = {
                    println(it.toString())
                    assert(it != null)
                },
                onError = { it.printStackTrace() }
        )
    }

}