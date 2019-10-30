package com.merp.android

import org.junit.jupiter.api.*
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions

class DatabaseTest {

    //why does adding an earning require declaring a member declaration?
    val test = Database.addEarning(Date(3,23,2019), "Job", BigDecimal(5), "I hate my job")

    @Test
    fun testGetDate(){
        Assertions.assertEquals("2019-03-23", Database.earning[0].getDate().getFullDate())
    }

    @Test
    fun testGetSource(){
        Assertions.assertEquals("Job", Database.earning[0].getSource())
    }

    @Test
    fun testGetAmount(){
        Assertions.assertEquals(BigDecimal(5), Database.earning[0].getAmount())
    }

    @Test
    fun testGetAddInfo(){
        Assertions.assertEquals("I hate my job", Database.earning[0].getAddInfo())
    }
}