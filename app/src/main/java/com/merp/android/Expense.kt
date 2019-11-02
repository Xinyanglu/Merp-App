package com.merp.android

import java.math.*

class Expense constructor( date: Date, category: String, price: BigDecimal, description: String, adi: String){
    private var p = price
    private var c = category
    private var d = date
    private var i = adi
    private var n = description

    //Getters
    fun getPrice(): BigDecimal{
        return p
    }

    fun getCategory(): String{
        return c
    }

    fun getDate(): String{
        return d.toString()
    }

    fun getAddInfo(): String{
        return i
    }

    fun getDescription(): String{
        return n
    }

    //Setters
    fun setPrice(price: BigDecimal){
        p = price
    }

    fun setCategory(category: String){
        c = category
    }

    fun setDate(date: Date){
        d = date
    }

    fun setAddInfo(adi: String){
        i = adi
    }

    fun setDescription(description: String){
        n = description
    }
}