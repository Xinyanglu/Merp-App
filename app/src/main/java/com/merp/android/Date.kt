package com.merp.android

import java.time.*

class Date constructor(year: Int, month: Int, day: Int){
    private var y = year.toString()

    private var m = if (month<10){
        "0${month}"
    }else {
        month.toString()
    }

    private var d = if (day<10){
        "0${day}"
    }else{
        day.toString()
    }

    private var date = LocalDate.parse("${y}-${m}-${d}")

    fun getMonth(): Int{
        return(date.monthValue)
    }

    fun getDay(): Int{
        return(date.dayOfMonth)
    }

    fun getYear(): Int{
        return(date.year)
    }

    fun getFullDate(): String{
        return date.toString()
    }
}