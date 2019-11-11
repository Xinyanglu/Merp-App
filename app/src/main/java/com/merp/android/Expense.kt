package com.merp.android

import java.math.*

class Expense(private var date: Date,
              private var category: String,
              private var amount: BigDecimal,
              private var addInfo: String){

    //Getters
    fun getAmount(): BigDecimal{
        return amount
    }

    fun getCategory(): String{
        return category
    }

    fun getDate(): Date{
        return date
    }

    fun getAddInfo(): String{
        return addInfo
    }

    //Setters
    fun setAmount(a: BigDecimal){
        amount = a
    }

    fun setCategory(c: String){
        category = c
    }

    fun setDate(d: Date){
        date = d
    }

    fun setAddInfo(adi: String){
        addInfo = adi
    }

    override fun toString(): String{
        return (getDate().getFullDate() + getCategory() + getAmount() + getAddInfo())
    }
}