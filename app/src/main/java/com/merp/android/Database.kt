package com.merp.android

import java.math.BigDecimal

object Database {
    val expense = ArrayList<Expense>()
    val earning = ArrayList<Earning>()

    fun addExpense(date: Date, category: String, price: BigDecimal, description: String, adi: String){
        expense.add(Expense(date,category,price,description,adi))
    }

    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String){
        earning.add(Earning(date,source,amount,adi))
    }
}