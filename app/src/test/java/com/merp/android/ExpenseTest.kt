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
        Assertions.assertEquals("Food", instance.getCategory())
    }

    @Test
    fun testGetPrice(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        Assertions.assertEquals(BigDecimal("300"), instance.getAmount())
    }

    @Test
    fun testGetAddInfo(){
        val instance = Expense(Date(2015, 9, 23), "Food", BigDecimal("300"), "This is not a description")
        Assertions.assertEquals("This is not a description", instance.getAddInfo())
    }
}