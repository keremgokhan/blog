package com.keremgokhan.blog.utils

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class DateUtilTest {

    @Test
    fun `formatDateHolocene should add 10000 years to Gregorian date`() {
        val date = LocalDateTime.of(2024, 1, 15, 10, 30)
        val result = DateUtil.formatDateHolocene(date)

        assertEquals("12024.1.15", result)
    }

    @Test
    fun `formatTime should format time correctly`() {
        val date = LocalDateTime.of(2024, 1, 15, 9, 5)
        val result = DateUtil.formatTime(date)

        assertEquals("09:05", result)
    }

    @Test
    fun `formatDate should format date correctly`() {
        val date = LocalDateTime.of(2024, 1, 15, 10, 30)
        val result = DateUtil.formatDate(date)

        assertEquals("2024.1.15", result)
    }

    @Test
    fun `formatDateHolocene should handle December correctly`() {
        val date = LocalDateTime.of(2024, 12, 31, 23, 59)
        val result = DateUtil.formatDateHolocene(date)

        assertEquals("12024.12.31", result)
    }
}
