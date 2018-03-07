package jp.eijenson.connpass_searcher.repository.table

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by kobayashimakoto on 2018/02/28.
 */
@Entity
data class UserTable(
        @Id var id: Long
        //val favorites: List<Int>
)