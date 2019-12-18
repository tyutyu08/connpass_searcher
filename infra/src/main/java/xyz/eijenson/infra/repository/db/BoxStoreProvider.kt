package xyz.eijenson.infra.repository.db

import android.content.Context
import xyz.eijenson.infra.repository.db.entity.MyObjectBox

class BoxStoreProvider(context: Context) {
    private val boxStore = MyObjectBox.builder().androidContext(context).build()

    fun provide() = boxStore
}