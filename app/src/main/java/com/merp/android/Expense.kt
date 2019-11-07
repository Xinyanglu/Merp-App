package com.merp.android

import java.math.*

class Expense(private var date: Date,
              private var category: String,
              private var price: BigDecimal,
              private var description: String,
              private var addInfo: String){

    //Getters
    fun getPrice(): BigDecimal{
        return price
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

    fun getDescription(): String{
        return description
    }

    //Setters
    fun setPrice(p: BigDecimal){
        price = p
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

    fun setDescription(desc: String){
        description = desc
    }
}