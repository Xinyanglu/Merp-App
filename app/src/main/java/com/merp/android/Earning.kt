package com.merp.android

import java.math.*
import com.merp.android.Date as Date

class Earning constructor(date: Date, source: String, amount: BigDecimal, adi: String){
    private var a = amount
    private var d = date
    private var i = adi
    private var s = source

    //Getters
    fun getAmount(): BigDecimal{
        return a
    }

    fun getDate(): Date{
        return d
    }

    fun getAddInfo(): String{
        return i
    }

    fun getSource(): String{
        return s
    }

    //Setters
    fun setAmount(amount: BigDecimal){
        a = amount
    }

    fun setDate(date: Date){
        d = date
    }

    fun setAddInfo(adi: String){
        i = adi
    }

    fun setSource(source: String){
        s = source
    }
}