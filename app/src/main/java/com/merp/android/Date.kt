package com.merp.android

import java.time.*

class Date constructor(month: Int, day: Int, year: Int) : Comparable<Date>{

    //Compares the dates and returns 1 which means larger, -1 means less, 0 means equal
    override fun compareTo(other: Date): Int{
        if (this.getYear()>other.getYear()) return 1
        if (this.getYear()<other.getYear()) return -1
        if (this.getMonth()<other.getMonth()) return -1
        if (this.getMonth()>other.getMonth())return 1
        if (this.getDay()>other.getDay()) return 1
        if (this.getDay()<other.getDay()) return -1
        return 0
    }

    //If month is less than 10, add a 0 in front of the string of it
    private var m = if (month<10){
        "0${month}"
    }else {
        month.toString()
    }

    //add 0 in front of day if less than 10
    private var d = if (day<10){
        "0${day}"
    }else{
        day.toString()
    }

    //stores the year string
    private var y = year.toString()

    //stores the date string
    private var date = LocalDate.parse("${y}-${m}-${d}")

    //returns the value of the month (Ex. January returns 1)
    fun getMonth(): Int{
        return(date.monthValue)
    }

    //returns Day of the date
    fun getDay(): Int{
        return(date.dayOfMonth)
    }

    //returns year of the date
    fun getYear(): Int{
        return(date.year)
    }

    //returns the entire date string
    fun getFullDate(): String{
        return date.toString()
    }
}