package com.merp.android

import org.junit.jupiter.api.*
import java.math.BigDecimal

class DatabaseTest {

    @Test
    fun testWriteEarning() {
        //if the test isn't working, completely erase everything in earnings.txt I'm lazy
        Database.addEarning(Date(2005, 5, 15), "Job", BigDecimal("500.52"), "add info")
        Database.addEarning(Date(3000, 2, 13), "Job", BigDecimal("500.52"), "add info")

        Database.earnings.clear()

        Database.initEarning()
        Assertions.assertEquals("2005-05-15@Job@500.52@add info", Database.earnings[0].toString())

    }
}