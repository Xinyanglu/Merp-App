package com.merp.android
import java.math.*
class Expense constructor(price: BigDecimal, category: String, date: String, adi: String){
    private var p = price
    private var c = category
    private var d = date
    private var i = adi

    fun getPrice(): BigDecimal{
        return p
    }

    fun getCategory(): String{
        return c
    }

    fun getDate(): String{
        return d
    }

    fun getAddInfo(): String{
        return i
    }
}