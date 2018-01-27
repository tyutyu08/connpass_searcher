package eijenson.connpass_searcher

import android.app.Application
import eijenson.connpass_searcher.repository.table.MyObjectBox
import io.objectbox.BoxStore

class App : Application() {

    lateinit var boxStore: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()

        boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}