package com.merp.android

import org.junit.jupiter.api.*
import java.math.BigDecimal

class ExpenseTest {

    @Test
    fun testGetDate(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        Assertions.assertEquals("2015-09-23", instance.getDate().getFullDate())
    }

    @Test
    fun testGetCategory(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        Assertions.assertEquals("Food", instance.getSource())
    }

    @Test
    fun testGetAmount(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        Assertions.assertEquals(BigDecimal("300"), instance.getAmount())
    }

    @Test
    fun testGetAddInfo(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        Assertions.assertEquals("This is not a description", instance.getAddInfo())
    }

    @Test
    fun testSetDate(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        instance.setDate(Date(2009, 2, 7))
        Assertions.assertEquals("2009-02-07", instance.getDate().getFullDate())
    }

    @Test
    fun testSetCategory(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        instance.setSource("Entertainment")
        Assertions.assertEquals("Entertainment", instance.getSource())
    }

    @Test
    fun testSetAmount(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        instance.setAmount(BigDecimal("420"))
        Assertions.assertEquals(BigDecimal("420"), instance.getAmount())
    }

    @Test
    fun testSetAddInfo(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        instance.setAddInfo("Information that is additional")
        Assertions.assertEquals("Information that is additional", instance.getAddInfo())
    }
}