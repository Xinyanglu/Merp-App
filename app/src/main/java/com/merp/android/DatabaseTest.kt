package com.merp.android

import org.junit.jupiter.api.*
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions

class DatabaseTest {

    //why does adding an earning require declaring a variable am I retarded
    val test = DatabaseObject.addEarning("October", "Job", BigDecimal(5), "I hate my job")

    @Test
    fun testGetDate(){
        Assertions.assertEquals("October", DatabaseObject.earning[0].getDate())
    }

    @Test
    fun testGetSource(){
        Assertions.assertEquals("Job", DatabaseObject.earning[0].getSource())
    }

    @Test
    fun testGetAmount(){
        Assertions.assertEquals(BigDecimal(5), DatabaseObject.earning[0].getAmount())
    }

    @Test
    fun testGetAddInfo(){
        Assertions.assertEquals("I hate my job", DatabaseObject.earning[0].getAddInfo())
    }
}