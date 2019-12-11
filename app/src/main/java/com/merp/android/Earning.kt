package com.merp.android

import java.math.*

class Earning(private var date: Date,
              private var source: String,
              private var amount: BigDecimal,
              private var addInfo: String) {

    //Getters
    fun getAmount(): BigDecimal{
        return amount
    }

    fun getDate(): Date{
        return date
    }

    fun getAddInfo(): String{
        return addInfo
    }

    fun getSource(): String{
        return source
    }

    //Setters
    fun setAmount(a: BigDecimal){
        amount = a
    }

    fun setDate(d: Date){
        date = d
    }

    fun setAddInfo(adi: String){
        addInfo = adi
    }

    fun setSource(c: String){
        source = c
    }

    fun toFile(): String{
        return (getDate().getFullDate() + "@" + getSource() + "@" + getAmount() + "@" + getAddInfo())
    }

    override fun toString(): String{
        return (getDate().getFullDate() + ", " + getSource() + ", " + getAmount() + ", " + getAddInfo())
    }
}