package jp.eijenson.connpass_searcher.util

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by kobayashimakoto on 2018/05/02.
 */
class TimeUtilTest {
    @Test
    fun isMidNight_マイナス1_夜中じゃない() {
        val result = isMidnight(-1)
        assertEquals(result, false)
    }

    @Test
    fun isMidNight_0_夜中() {
        val result = isMidnight(0)
        assertEquals(result, true)
    }

    @Test
    fun isMidNight_6_夜中() {
        val result = isMidnight(6)
        assertEquals(result, true)
    }

    @Test
    fun isMidNight_7_夜中じゃない() {
        val result = isMidnight(7)
        assertEquals(result, false)
    }

    @Test
    fun isMidNight_20_夜中じゃない() {
        val result = isMidnight(20)
        assertEquals(result, false)
    }

    @Test
    fun isMidNight_21_夜中() {
        val result = isMidnight(21)
        assertEquals(result, true)
    }

    @Test
    fun isMidNight_24_夜中() {
        val result = isMidnight(24)
        assertEquals(result, true)
    }

    @Test
    fun isMidNight_25_夜中じゃない() {
        val result = isMidnight(25)
        assertEquals(result, false)
    }
}