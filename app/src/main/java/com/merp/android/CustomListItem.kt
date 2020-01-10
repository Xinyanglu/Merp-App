package com.merp.android

/**
 * Constructs an instance of the CustomListItem class.
 * CustomListItems are constructed using the properties of an existing Earning/Expense object.
 *
 * @property [tvDate] The String value of either [Earning.date] or [Expense.date]
 * @property [tvSource] The String value of either [Earning.source] or [Expense.source]
 * @property [tvAmount] The String value of either [Earning.amount] or [Expense.amount]
 * @property [tvAddInfo] The String value of either [Earning.addInfo] or [Expense.addInfo]
 */
class CustomListItem(private val tvDate: String,
                     private val tvSource: String,
                     private val tvAmount: String,
                     private val tvAddInfo: String) {

    /** Returns the instance's [tvDate] property */
    fun getTVDate(): String{
        return tvDate
    }

    /** Returns the instance's [tvSource] property */
    fun getTVSource(): String{
        return tvSource
    }

    /** Returns the instance's [tvAmount] property */
    fun getTVAmount(): String{
        return tvAmount
    }

    /** Returns the instance's [tvAddInfo] property */
    fun getTVAddInfo(): String{
        return tvAddInfo
    }
}