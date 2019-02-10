package jp.eijenson.connpass_searcher.infra.repository.api

import io.reactivex.rxkotlin.subscribeBy
import jp.eijenson.connpass_searcher.infra.repository.api.entity.RequestEvent
import org.junit.Ignore
import org.junit.Test

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