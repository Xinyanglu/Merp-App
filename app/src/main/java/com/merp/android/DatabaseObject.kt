package com.merp.android

import java.math.BigDecimal

/**
 * Note on Object files vs. Class files with companion objects:
 *
 * Object files acts similarly to classes, but are never instantiated
 * Instead, the object file itself is the one and only instance, and can be used as a static instance
 *
 * Alternatively, this file could be a class file (class DatabaseObject), but with a companion object
 * To create a companion object, add
 *      companion object optionalName {}
 * With the code inside the brackets
 */
object DatabaseObject {
    val expense = ArrayList<Expense>()
    val earning = ArrayList<Earning>()

    fun addEarning(date: String, source: String, amount: BigDecimal, adi: String){
        earning.add(Earning(date,source,amount,adi))
    }

    fun addExpense(date: String, category: String, price: BigDecimal, description: String, adi: String){
        expense.add(Expense(date,category,price,description,adi))
    }
}