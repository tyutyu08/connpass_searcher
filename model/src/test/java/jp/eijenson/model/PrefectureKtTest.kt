package jp.eijenson.model

import jp.eijenson.model.Prefecture.Companion.getPreference
import org.junit.Test

/**
 * Created by kobayashimakoto on 2018/02/10.
 */
class PrefectureKtTest {
    @Test
    fun getPreference_Tokyo() {
        val str = "東京都"
        val preference = getPreference(str)
        assert(preference == Prefecture.TOKYO)
    }

}