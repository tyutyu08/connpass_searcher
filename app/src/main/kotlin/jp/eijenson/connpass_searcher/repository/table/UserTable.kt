package jp.eijenson.connpass_searcher.repository.table

import io.objectbox.annotation.Entity

/**
 * Created by kobayashimakoto on 2018/02/28.
 */
@Entity
data class UserTable(
        val id: Long,
        val favorites: List<Int>
)