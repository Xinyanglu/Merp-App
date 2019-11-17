package com.merp.android

import java.io.*
import java.math.BigDecimal
import com.merp.android.Date as Date
import com.merp.android.Earning as Earning
import com.merp.android.Expense as Expense

object Database {
    var expense = ArrayList<Expense>()
    var earning =  ArrayList<Earning>()

    init{
        //uses regex to split up every line into date, source, amount, and additional info
        initEarning()

        //initializes expenses array list
        initExpense()
    }

    fun writeExpense(){
        val w = File("expenses.txt").bufferedWriter()
        w.use{ out->
            for (i in expense){
                out.write(i.toString() + "\n")
            }
        }
        w.close()
    }

    fun writeEarning(){
        val w = File("earnings.txt").bufferedWriter()
        w.use{out->
            for (i in earning){
                out.write(i.toString() + "\n")
            }

        }
        w.close()
    }

    fun addExpense(date: Date, category: String, price: BigDecimal, adi: String){
        expense.add(searchExpense(date,expense,0,expense.size-1), Expense(date, category, price, adi))
        writeExpense()

    }

    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String){
        earning.add(searchEarning(date, earning,0,earning.size-1), Earning(date, source, amount, adi))
        writeEarning()
    }

    //returns the index value of the array to include in the range
    fun searchEarning(date: Date, list: ArrayList<Earning>, min: Int, max: Int): Int{
        var i = (max+min)/2
        var d = list[i].getDate()
        if (max-min==1) {
            return if (d.compareTo(date) == 1) i + 1
            else i
        }

        return when (d.compareTo(date)) {
            1 ->  searchEarning(date, list, i, max)
            -1 ->  searchEarning(date, list, min, i)
            else -> i
        }
    }

    fun searchExpense(date: Date, list: ArrayList<Expense>, min: Int, max: Int): Int{
        var i = (max+min)/2
        var d = list[i].getDate()
        if (max-min==1) {
            return if (d.compareTo(date) == 1) i + 1
            else i
        }

        return when (d.compareTo(date)) {
            1 ->  searchExpense(date, list, i, max)
            -1 ->  searchExpense(date, list, min, i)
            else -> i
        }
    }

    //returns a list of all the dates that are between the start and end date
    fun searchRangeEarning(start: Date, end: Date, list: ArrayList<Earning>): MutableList<Earning>{
        return list.subList(searchEarning(start,list,0,list.size-1),searchEarning(end,list,0,list.size-1)+1)
    }

    fun searchRangeExpense(start: Date, end: Date, list: ArrayList<Expense>): MutableList<Expense>{
        return list.subList(searchExpense(start,list,0,list.size-1), searchExpense(end,list,0,list.size-1)+1)
    }

    //Open expenses text file to read each line and add each line of information to the expense array list
    fun initExpense(){
        val f = File(".\\expenses.txt").bufferedReader()
        f.forEachLine {
            val text = it
            val pattern = "[^@]+".toRegex() // regex pattern where the character is not the '@' symbol which is what we use to separate info
            val found = pattern.findAll(text).toList()
            val date = Date(found[0].value)
            val source = found[1].value
            val amount = BigDecimal(found[2].value)
            val addInfo = found[3].value
            expense.add(Expense(date, source, amount, addInfo))
        }
        f.close()
    }

    //Open earnings text file to read each line and add the information to the earning array list
    fun initEarning(){
        val f = File(".\\earnings.txt").bufferedReader()
        f.forEachLine {
            val text = it
            val pattern = "[^@]+".toRegex()
            val found = pattern.findAll(text).toList()
            val date = Date(found[0].value)
            val source = found[1].value
            val amount = BigDecimal(found[2].value)
            val addInfo = found[3].value
            earning.add(Earning(date,source,amount,addInfo))
        }
        f.close()
    }
}