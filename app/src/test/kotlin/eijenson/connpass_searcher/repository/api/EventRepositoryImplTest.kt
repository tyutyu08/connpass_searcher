package eijenson.connpass_searcher.repository.api

import eijenson.connpass_searcher.repository.entity.RequestEvent
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Ignore
import org.junit.Test

class EventRepositoryImplTest {
    @Test
    @Ignore
    fun addition_isCorrect() {
        val repository = EventRepositoryImpl()
        repository.getEvent(RequestEvent(keyword = "Android")).subscribeBy(
                onNext = {
                    println(it.toString())
                    assert(it != null)
                },
                onError = { it.printStackTrace() }
        )
    }

}