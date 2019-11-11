package com.merp.android

import java.io.File
import java.math.BigDecimal
import com.merp.android.Date as Date
import com.merp.android.Earning as Earning
import com.merp.android.Expense as Expense

object Database {
    var expense = ArrayList<Expense>()
    var earning =  ArrayList<Earning>()

    fun initLists(){
        File("earnings.txt").forEachLine {
            var text = it
            val pattern = "[^@]+".toRegex()
            val found = pattern.findAll(text).toList()
            val date = Date(found[0].value)
            val source = found[1].value
            val amount = BigDecimal(found[2].value.toString())
            val addInfo = found[3].value
            earning.add(Earning(date,source,amount,addInfo))
        }
    }

    fun addExpense(date: Date, category: String, price: BigDecimal, adi: String){
        expense.add(Expense(date, category, price, adi))
    }

    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String) {
        earning.add(Earning(date, source, amount, adi))
    }

    //returns the index value of the array to include in the range
    fun search(date: Date, list: ArrayList<Earning>, min: Int, max: Int): Int{
        var i = (max+min)/2
        var d = list[i].getDate()
        if (max-min==1) {
            return if (d.compareTo(date) == 1) i + 1
            else i
        }

        return when (d.compareTo(date)) {
            1 ->  search(date, list, i, max)
            -1 ->  search(date, list, min, i)
            else -> i
        }
    }

    //returns a list of all the dates that are between the start and end date
    fun searchDateRange(start: Date, end: Date, list: ArrayList<Earning>): MutableList<Earning>{
        return list.subList(search(start,list,0,list.size-1),search(end,list,0,list.size-1)+1)
    }
}