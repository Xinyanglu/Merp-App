package com.merp.android

import java.time.*

class Date constructor(month: Int, day: Int, year: Int){
    var m = if (month<10){
        "0${month.toString()}"
    }else{
        month.toString()
    }

    var y = year.toString()

    var d = if (day<10){
        "0${day.toString()}"
    }else{
        day.toString()
    }

    private var date = LocalDate.parse((y) + "-" + (m) + "-" + (d))

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