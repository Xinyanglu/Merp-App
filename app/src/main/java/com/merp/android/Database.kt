package com.merp.android

import android.os.Environment
import android.util.Log
import java.io.*
import java.math.BigDecimal
import com.merp.android.Date as Date
import com.merp.android.Earning as Earning
import com.merp.android.Expense as Expense

object Database {
    private val expense = ArrayList<Expense>()
    private val earning = ArrayList<Earning>()
    private val expensesSources = ArrayList<String>()
    private val earningsSources = ArrayList<String>()

    private var earnings_file = ""
    private var expenses_file = ""
    private var earnings_sources_file = ""
    private var expenses_sources_file = ""

    fun setDirectories(earningsDir: String, expensesDir: String, earnSourcesDir: String, expSourcesDir: String) {
        earnings_file = earningsDir
        expenses_file = expensesDir
        earnings_sources_file = earnSourcesDir
        expenses_sources_file = expSourcesDir
    }

    fun getEarnings(): ArrayList<Earning>{
        return earning
    }

    fun getExpenses(): ArrayList<Expense>{
        return expense
    }

    fun getEarningsSources(): ArrayList<String>{
        return earningsSources
    }

    fun getExpensesSources(): ArrayList<String>{
        return expensesSources
    }

    fun writeExpenses(){
        val w = BufferedWriter(FileWriter(expenses_file, false))
        w.use{ out->
            for (i in expense){
                out.write(i.toFile() + "\n")

            }
        }
        w.close()
    }

    fun writeEarnings(){
        val w = BufferedWriter(FileWriter(earnings_file, false))
        w.use{out->
            for (i in earning){
                out.write(i.toFile() + "\n")
            }
        }
        w.close()
    }

    fun writeExpensesSources(){
        val w = BufferedWriter(FileWriter(expenses_sources_file, false))
        w.use {
            for(i in expensesSources){
                it.write(i + "\n")
            }
        }
    }

    fun writeEarningsSources(){
        val w = BufferedWriter(FileWriter(earnings_sources_file, false))
        w.use {
            for(i in earningsSources){
                it.write(i + "\n")
            }
        }
    }

    //adds expense to expenses arraylist
    fun addExpense(date: Date, category: String, price: BigDecimal, adi: String){
        expense.add(searchExpenses(date,expense,0,expense.size-1), Expense(date, category, price, adi))
        writeExpenses()
    }

    //adds the earning into the earnings array list and
    //opens the earnings text file and writes everything inside the arraylist into it
    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String){
        earning.add(searchEarnings(date, earning,0, earning.size-1), Earning(date, source, amount, adi))
        writeEarnings()
    }

    //adds the expense source to the arraylist and opens
    // expenses source text file and writes everything inside expenses sources file into it
    fun addExpensesSource(source: String){
        expensesSources.add(searchExpensesSources(source, 0, expensesSources.size-1), source)
        writeExpensesSources()
    }

    //opens earnings sources text file and writes
    // everything in the earnings source arraylist into it
    fun addEarningsSource(source: String){
        earningsSources.add(searchEarningsSources(source, 0, earningsSources.size-1), source)
        writeEarningsSources()
    }

    //searches through earnings array list and returns the index of where the date should be inserted in to preserve order

    fun searchEarnings(date: Date, list: ArrayList<Earning>, min: Int, max: Int): Int{
        if(earning.isEmpty()) return 0
        if (date.compareTo(list[max].getDate()) == 1){
            return list.size
        }
        var i = (max+min)/2
        var d = list[i].getDate()
        if (max-min<=1) {
            return if (date.compareTo(d) == 1) i + 1
            else i
        }

        return when (date.compareTo(d)) {
            1 ->  searchEarnings(date, list, i, max)
            -1 ->  searchEarnings(date, list, min, i)
            else -> i
        }
    }

    //searches through the expenses array list and returns the index of the array of where the date should go to preserve order
    fun searchExpenses(date: Date, list: ArrayList<Expense>, min: Int, max: Int): Int{
        if(expense.isEmpty()) return 0
        if (date.compareTo(list[max].getDate()) == 1){
            return list.size
        }
        var i = (max+min)/2
        var d = list[i].getDate()
        if (max-min<=1) {
            return if (date.compareTo(d) == 1) i + 1
            else i
        }

        return when (date.compareTo(d)) {
            1 ->  searchExpenses(date, list, i, max)
            -1 ->  searchExpenses(date, list, min, i)
            else -> i
        }
    }

    //searches through the expenses sources to put the source in alphabetically to preserve order
    fun searchExpensesSources(source: String, min: Int, max: Int): Int{
        if(expensesSources.isEmpty()) return 0
        else{
            var i: Int = (max+min)/2
            var s: String = expensesSources[i]

            if(max-min <= 1){
                return if(source.compareTo(s) == 1) i+1
                else i
            }else {
                return when (source.compareTo(s)) {
                    1 -> searchExpensesSources(source, i, max)
                    -1 -> searchExpensesSources(source, min, i)
                    else -> i
                }
            }
        }
    }

    //searches through earnings sources to put the source in alphabetically to preserve the order.
    fun searchEarningsSources(source: String, min: Int, max: Int): Int{
        if(earningsSources.isEmpty()) return 0
        else{
            var i: Int = (max+min)/2
            var s: String = earningsSources[i]

            if(max-min <= 1){
                return if(source.compareTo(s) == 1) i+1
                else i
            }else {
                return when (source.compareTo(s)) {
                    1 -> searchEarningsSources(source, i, max)
                    -1 -> searchEarningsSources(source, min, i)
                    else -> i
                }
            }
        }
    }

    //Open expenses text file to read each line and add each line of information to the expense array list
    fun readExpenses(){
        expense.clear()
        val f = BufferedReader(FileReader(expenses_file))
        f.forEachLine {
            val text = it
            if (text.isNotBlank()) {
                val pattern =
                    "[^@]+".toRegex() // regex pattern where the character is not the '@' symbol which is what we use to separate info
                val found = pattern.findAll(text).toList()
                val date = Date(found[0].value)
                val source = found[1].value
                val amount = BigDecimal(found[2].value)
                var addInfo = ""
                try{
                    addInfo = " " + found[3].value
                }catch(e: Exception){}
                expense.add(Expense(date, source, amount, addInfo))
            }
        }
        f.close()
    }

    //Open earnings text file to read each line and add the information to the earning array list
    fun readEarnings(){
        earning.clear()
        val r = BufferedReader(FileReader(earnings_file))
        r.forEachLine {
            val text = it
            if (text.isNotBlank()) {
                val pattern = "[^@]+".toRegex()
                val found = pattern.findAll(text).toList()
                val date = Date(found[0].value)
                val source = found[1].value
                val amount = BigDecimal(found[2].value)
                var addInfo = ""
                try{
                    addInfo = found[3].value
                }catch(e: Exception){}
                earning.add(Earning(date, source, amount, addInfo))
            }
        }
        r.close()
    }

    //reads the expenses sources in the file
    fun readExpensesSources(){
        expensesSources.clear()
        val r = BufferedReader(FileReader((expenses_sources_file)))
        r.forEachLine {
            expensesSources.add(it)
        }
    }

    //reads the earnings sources in the file
    fun readEarningsSources(){
        earningsSources.clear()
        val r = BufferedReader(FileReader(earnings_sources_file))
        r.forEachLine {
            earningsSources.add(it)
        }
    }

    //returns a list of all the dates that are between the start and end date
    fun searchRangeEarnings(start: Date, end: Date): MutableList<Earning>{
        return earning.subList(searchEarnings(start,earning,0,earning.size-1),searchEarnings(end,earning,0,earning.size-1)+1)
    }

    fun searchRangeExpenses(start: Date, end: Date): MutableList<Expense>{
        return expense.subList(searchExpenses(start,expense,0,expense.size-1), searchExpenses(end,expense,0,expense.size-1)+1)
    }
}