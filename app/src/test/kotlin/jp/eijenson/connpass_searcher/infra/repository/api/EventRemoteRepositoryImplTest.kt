package jp.eijenson.connpass_searcher.infra.repository.api

import io.reactivex.rxkotlin.subscribeBy
import jp.eijenson.model.RequestEvent
import org.junit.Ignore
import org.junit.Test
import xyz.eijenson.infra.repository.api.EventApiRepository

class EventRemoteRepositoryImplTest {
    @Test
    @Ignore
    fun addition_isCorrect() {
        val repository = EventApiRepository()
        repository.getAll(RequestEvent(keyword = "Android")).subscribeBy(
            onSuccess = {
                println(it.toString())
                assert(it != null)
            },
            onError = { it.printStackTrace() }
        )
    }
}