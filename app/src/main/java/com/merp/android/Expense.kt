package com.merp.android

import java.math.*

class Expense(private var date: Date,
              private var source: String,
              private var amount: BigDecimal,
              private var addInfo: String){

    //Getters
    fun getAmount(): BigDecimal{
        return amount
    }

    fun getSource(): String{
        return source
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

    fun setSource(c: String){
        source = c
    }

    fun setDate(d: Date){
        date = d
    }

    fun setAddInfo(adi: String){
        addInfo = adi
    }

    fun toFile(): String{
        return (getDate().getFullDate() + "@" + getSource() + "@" + getAmount() + "@" + getAddInfo())
    }

    override fun toString(): String{
        return (getDate().getFullDate() + ", " + getSource() + ", " + getAmount() + ", " + getAddInfo())
    }
}