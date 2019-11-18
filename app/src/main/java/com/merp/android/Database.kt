package com.merp.android

import android.os.Environment
import android.util.Log
import java.io.*
import java.math.BigDecimal
import com.merp.android.Date as Date
import com.merp.android.Earning as Earning
import com.merp.android.Expense as Expense

object Database {
    var expense = ArrayList<Expense>()
    var earning =  ArrayList<Earning>()

    //private const val earnings_file = "src/main/java/com/merp/android/earnings.txt"
    //private const val expenses_file = "src/main/java/com/merp/android/expenses.txt"

    private var earnings_file = ""
    private var expenses_file = ""

    /**
     * --------------------HELLO----------------------
     * as far as i can tell, this is working properly
     * the values are found in MainActivity.kt
     * i left the init and old file dir variables commented out just in case
     *
     * HOWEVER: i dont think this is saving to the same earnings/expenses.txt files that we made
     * if you use the "File" app on the AVD and press "Show Internal Storage" in the settings and navigate to it
     * you should see find "entries>earnings.txt" under "Android SDK built for x86 or something like that
     *
     * but it seems to be read/writing properly with this other file anyways
     * I HAVENT TESTED IF THE EXPENSE.TXT WORKS i just threw it in after seeing the earnings.txt work
     *
     * its 12:53am fuck me in the ass cuz i love jesus
     *
     * actually i got a merge conflict when i had to pull before pushing and
     * im not going to bother to check if it still works so hf with that
     */
    fun setDirectory(earningsDir: String, expensesDir: String) {
        earnings_file = earningsDir
        expenses_file = expensesDir
    }

    fun getEarnings(): ArrayList<Earning>{
        return earning
    }

    fun getExpenses(): ArrayList<Expense>{
        return expense
    }


    fun writeExpense(){
        val w = BufferedWriter(FileWriter(expenses_file, false))
        w.use{ out->
            for (i in expense){
                out.write(i.toFile() + "\n")

            }
        }
        w.close()
    }

    fun writeEarning(){
        val w = BufferedWriter(FileWriter(earnings_file, false))
        w.use{out->
            for (i in earning){
                out.write(i.toFile() + "\n")
            }
        }
        w.close()
    }

    fun addExpense(date: Date, category: String, price: BigDecimal, adi: String){
        expense.add(searchExpense(date,expense,0,expense.size-1), Expense(date, category, price, adi))
        writeExpense()
    }

    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String){
        earning.add(searchEarning(date, earning,0, earning.size-1), Earning(date, source, amount, adi))
        writeEarning()
    }

    //returns the index value of the array to include in the range
    fun searchEarning(date: Date, list: ArrayList<Earning>, min: Int, max: Int): Int{
        if(earning.isEmpty()) return 0
        var i = (max+min)/2
        var d = list[i].getDate()
        if (max-min<=1) {
            return if (date.compareTo(d) == 1) i + 1
            else i
        }

        return when (date.compareTo(d)) {
            1 ->  searchEarning(date, list, i, max)
            -1 ->  searchEarning(date, list, min, i)
            else -> i
        }
    }

    fun searchExpense(date: Date, list: ArrayList<Expense>, min: Int, max: Int): Int{
        if(expense.isEmpty()) return 0
        var i = (max+min)/2
        var d = list[i].getDate()
        if (max-min<=1) {
            return if (date.compareTo(d) == 1) i + 1
            else i
        }

        return when (date.compareTo(d)) {
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
    fun initEarning(){
        earning.clear()
        val f = BufferedReader(FileReader(earnings_file))
        f.forEachLine {
            val text = it
            if (text.isNotBlank()) {
                val pattern = "[^@]+".toRegex()
                val found = pattern.findAll(text).toList()
                val date = Date(found[0].value)
                val source = found[1].value
                val amount = BigDecimal(found[2].value)
                val addInfo = found[3].value
                earning.add(Earning(date, source, amount, addInfo))
            }
        }
        f.close()
    }
}