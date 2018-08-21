package jp.eijenson.connpass_searcher.infra.repository.api

import jp.eijenson.connpass_searcher.infra.entity.RequestEvent
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Ignore
import org.junit.Test

class EventRepositoryImplTest {
    @Test
    @Ignore
    fun addition_isCorrect() {
        val repository = EventRepositoryImpl()
        repository.getAll(RequestEvent(keyword = "Android")).subscribeBy(
                onNext = {
                    println(it.toString())
                    assert(it != null)
                },
                onError = { it.printStackTrace() }
        )
    }

}