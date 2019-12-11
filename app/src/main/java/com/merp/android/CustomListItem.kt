package com.merp.android

class CustomListItem(private val tvDate: String,
                     private val tvSource: String,
                     private val tvAmount: String,
                     private val tvAddInfo: String) {

    fun getTVDate(): String{
        return tvDate
    }

    fun getTVSource(): String{
        return tvSource
    }

    fun getTVAmount(): String{
        return tvAmount
    }

    fun getTVAddInfo(): String{
        return tvAddInfo
    }
}