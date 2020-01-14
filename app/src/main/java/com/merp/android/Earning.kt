package com.merp.android

import java.math.*

/**
 * Constructs an instance of the Earning class which records the
 * [date] of the earning, [source] of the earning, [amount] earned, and any [addInfo].
 */
class Earning(private var date: Date,
              private var source: String,
              private var amount: BigDecimal,
              private var addInfo: String) {

    /** Returns the instance's [date] property */
    fun getDate(): Date{
        return date
    }

    /** Returns the instance's [source] property */
    fun getSource(): String{
        return source
    }

    /** Returns the instance's [amount] property */
    fun getAmount(): BigDecimal{
        return amount
    }

    /** Returns the instance's [addInfo] property */
    fun getAddInfo(): String{
        return addInfo
    }

    /** Sets the instance's [date] property to [d] */
    fun setDate(d: Date){
        date = d
    }

    /** Sets the instance's [source] property [s] */
    fun setSource(s: String){
        source = s
    }

    /** Sets the instance's [amount] property to [a] */
    fun setAmount(a: BigDecimal){
        amount = a
    }

    /** Sets the instance's [addInfo] property [adi] */
    fun setAddInfo(adi: String){
        addInfo = adi
    }

    /**
     * Returns all of the instance's properties.
     * Each property is separated by an "@" symbol for use as the regex for string splitting.
     */
    fun toFile(): String{
        return (getDate().getFullDate() + "@" + getSource() + "@" + getAmount() + "@" + getAddInfo())
    }

    /**
     * Returns all of the instance's properties.
     * Each property is separated by commas for better readability.
     */
    override fun toString(): String{
        return (getDate().getFullDate() + ", " + getSource() + ", " + getAmount() + ", " + getAddInfo())
    }
}