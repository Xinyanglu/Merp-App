package com.merp.android

import java.math.*

class Earning constructor(amount: BigDecimal, date: String, adi: String){
    private var a = amount
    private var d = date
    private var i = adi

    fun getAmount(): BigDecimal{
        return a
    }

    fun getDate(): String{
        return d
    }

    fun getAddInfo(): String{
        return i
    }


}