package com.merp.android

import org.junit.jupiter.api.*
import java.math.BigDecimal

class DatabaseTest {

    @Test
    fun testWriteEarning() {
        Database.addEarning(Date(2005, 5, 15), "Job", BigDecimal("500.52"), "add info")

        Database.earning.clear()

        Database.initEarning()

        Assertions.assertEquals("2005-05-15@Job@500.52@add info", Database.earning[0].toString())
    }
}