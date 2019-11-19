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

    //private const val earnings_file = "src/main/java/com/merp/android/earnings.txt"
    //private const val expenses_file = "src/main/java/com/merp/android/expenses.txt"

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

    fun addExpense(date: Date, category: String, price: BigDecimal, adi: String){
        expense.add(searchExpenses(date,expense,0,expense.size-1), Expense(date, category, price, adi))
        writeExpenses()
    }

    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String){
        earning.add(searchEarnings(date, earning,0, earning.size-1), Earning(date, source, amount, adi))
        writeEarnings()
    }

    fun addExpensesSource(source: String){
        expensesSources.add(searchExpensesSources(source, 0, expensesSources.size-1), source)
        writeExpensesSources()
    }

    fun addEarningsSource(source: String){
        earningsSources.add(searchEarningsSources(source, 0, earningsSources.size-1), source)
        writeEarningsSources()
    }

    //returns the index value of the array to include in the range
    fun searchEarnings(date: Date, list: ArrayList<Earning>, min: Int, max: Int): Int{
        if(earning.isEmpty()) return 0
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

    fun searchExpenses(date: Date, list: ArrayList<Expense>, min: Int, max: Int): Int{
        if(expense.isEmpty()) return 0
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
                val addInfo = found[3].value
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
                val addInfo = found[3].value
                Log.d("EMPTYADDINFO?", addInfo)
                earning.add(Earning(date, source, amount, addInfo))
            }
        }
        r.close()
    }

    fun readExpensesSources(){
        expensesSources.clear()
        val r = BufferedReader(FileReader((expenses_sources_file)))
        r.forEachLine {
            expensesSources.add(it)
        }
    }

    fun readEarningsSources(){
        earningsSources.clear()
        val r = BufferedReader(FileReader(earnings_sources_file))
        r.forEachLine {
            earningsSources.add(it)
        }
    }

    //returns a list of all the dates that are between the start and end date
    fun searchRangeEarnings(start: Date, end: Date, list: ArrayList<Earning>): MutableList<Earning>{
        return list.subList(searchEarnings(start,list,0,list.size-1),searchEarnings(end,list,0,list.size-1)+1)
    }

    fun searchRangeExpenses(start: Date, end: Date, list: ArrayList<Expense>): MutableList<Expense>{
        return list.subList(searchExpenses(start,list,0,list.size-1), searchExpenses(end,list,0,list.size-1)+1)
    }
}