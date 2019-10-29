package com.merp.android

import java.math.BigDecimal

class Database {
    val expense = ArrayList<Expense>()
    val earning = ArrayList<Earning>()

    fun addExpense(date: String, category: String, price: BigDecimal, description: String, adi: String){
        expense.add(Expense(date,category,price,description,adi))
    }

    fun addEarning(date: String, source: String, amount: BigDecimal, adi: String){
        earning.add(Earning(date,source,amount,adi))
    }
}