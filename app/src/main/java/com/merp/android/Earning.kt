package com.merp.android

import java.math.*
import com.merp.android.Date as Date

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

    fun setSource(s: String){
        source = s
    }

    override fun toString(): String{
        return (this.getDate().getFullDate() + getSource() + getAmount() + getAddInfo())
    }
}