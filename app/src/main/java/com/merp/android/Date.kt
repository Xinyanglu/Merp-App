package com.merp.android

import java.time.*

class Date : Comparable<Date>{
    private var m = ""
    private var y = ""
    private var d = ""
    private var date : LocalDate

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

    constructor(year: Int, month: Int, day: Int){
        //If month is less than 10, add a 0 in front of the string of it
        m = if (month<10){
            "0${month}"
        }else {
            month.toString()
        }

        //If day is less than 10, add 0 in front of the string
        d = if (day<10){
            "0${day}"
        }else{
            day.toString()
        }

        //stores the year as a string
        y = year.toString()

        date = LocalDate.parse("${y}-${m}-${d}")
    }

    constructor(dateString: String){
        date = LocalDate.parse(dateString)
    }

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

    fun toFloat(): Float{
        return ("" + getYear() + getMonth() + getDay()).toFloat()
    }
}