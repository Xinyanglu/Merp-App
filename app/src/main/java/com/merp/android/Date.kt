package com.merp.android

import java.time.*

class Date constructor(month: Int, day: Int, year: Int){
    private var date = LocalDate.parse((year as String) + "-" + (month as String) + "-" + (day as String))

    fun getMonth(): Month{
        return(date.month)
    }

    fun getDay(): Int{
        return(date.dayOfMonth)
    }

    fun getYear(): Int{
        return(date.year)
    }



}