package com.merp.android

import java.time.*

/**
 * A custom comparable class for recording dates in "yyyy-MM-dd" format.
 * Utilizes the [LocalDate] class.
 */
class Date : Comparable<Date>{
    /** A [LocalDate] object */
    private var date : LocalDate

    /**
     * Compares two [Date] objects chronologically.
     * Returns 1 if this object's date comes after, -1 if it comes before, and 0 if the two dates are the same.
     *
     * @param [other] The date to compare to
     */
    override fun compareTo(other: Date): Int{
        if (this.getYear()>other.getYear()) return 1
        if (this.getYear()<other.getYear()) return -1
        if (this.getMonth()<other.getMonth()) return -1
        if (this.getMonth()>other.getMonth())return 1
        if (this.getDay()>other.getDay()) return 1
        if (this.getDay()<other.getDay()) return -1
        return 0
    }

    /**
     * Constructs a Date object using year, month, and day integer values.
     *
     * @param [y] The year
     * @param [m] The month
     * @param [d] The day
     */
    constructor(y: Int, m: Int, d: Int){
        //If month is less than 10, add a 0 in front of the string of it
        val month = if (m<10){
            "0${m}"
        }else {
            m.toString()
        }

        //If day is less than 10, add 0 in front of the string
        val day = if (d<10){
            "0${d}"
        }else{
            d.toString()
        }

        //stores the year as a string
        val year = y.toString()

        date = LocalDate.parse("${year}-${month}-${day}")
    }

    /**
     * Constructs a Date object using a String in "yyyy-MM-dd" format.
     *
     * @param [dateString] A String in "yyyy-MM-dd" format
     */
    constructor(dateString: String){
        date = LocalDate.parse(dateString)
    }

    /** Returns the integer value of the month (Ex. January returns 1) */
    fun getMonth(): Int{
        return(date.monthValue)
    }

    /** Returns the integer value of the day */
    fun getDay(): Int{
        return(date.dayOfMonth)
    }

    /** Returns the year */
    fun getYear(): Int{
        return(date.year)
    }

    /** Returns the [date] as a String in "yyyy-MM-dd" format */
    fun getFullDate(): String{
        return date.toString()
    }

    /** Returns the [date]'s float value of "yyyyMMdd" */
    fun toFloat(): Float{
        return ("" + getYear() + getMonth() + getDay()).toFloat()
    }
}