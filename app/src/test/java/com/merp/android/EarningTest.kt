package com.merp.android

import org.junit.jupiter.api.*
import java.math.BigDecimal

class EarningTest {

    @Test
    fun testGetDate(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        Assertions.assertEquals("2019-03-23", instance.getDate().getFullDate())
    }

    @Test
    fun testGetSource(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        Assertions.assertEquals("Job", instance.getSource())
    }

    @Test
    fun testGetAmount(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        Assertions.assertEquals(BigDecimal("5"), instance.getAmount())
    }

    @Test
    fun testGetAddInfo(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        Assertions.assertEquals("I hate my job", instance.getAddInfo())
    }

    @Test
    fun testSetDate(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        instance.setDate(Date(2003,12,19))
        Assertions.assertEquals("2003-12-19", instance.getDate().getFullDate())
    }

    @Test
    fun testSetSource(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        instance.setSource("Gambling")
        Assertions.assertEquals("Gambling", instance.getSource())
    }

    @Test
    fun testSetAmount(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        instance.setAmount(BigDecimal("2000.35"))
        Assertions.assertEquals(BigDecimal("2000.35"), instance.getAmount())
    }

    @Test
    fun testSetAddInfo(){
        val instance = Earning(Date(2019,3,23), "Job", BigDecimal("5"), "I hate my job")
        instance.setAddInfo("Bless RNGesus")
        Assertions.assertEquals("Bless RNGesus", instance.getAddInfo())
    }
}