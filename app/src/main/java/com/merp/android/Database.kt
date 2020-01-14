package com.merp.android

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

    fun setDirectories(
        earningsDir: String,
        expensesDir: String,
        earnSourcesDir: String,
        expSourcesDir: String
    ) {
        earnings_file = earningsDir
        expenses_file = expensesDir
        earnings_sources_file = earnSourcesDir
        expenses_sources_file = expSourcesDir
    }

    /**
    Returns the earning array list

    Return:
    earning: ArrayList<Earning>
     **/

    fun getEarnings(): ArrayList<Earning> {
        return earning
    }

    /**
    Returns the expense array list

    Return:
    expense: ArrayList<Expense>
     **/
    fun getExpenses(): ArrayList<Expense> {
        return expense
    }

    /**
    Returns the earnings sources array list

    Return:
    earningsSources: ArrayList<String>
     **/
    fun getEarningsSources(): ArrayList<String> {
        return earningsSources
    }

    /**
    Returns the expense sources array list

    Return:
    expensesSources: ArrayList<String>
     **/
    fun getExpensesSources(): ArrayList<String> {
        return expensesSources
    }

    //Writes all expenses from expense array list into expenses_file.
    fun writeExpenses() {
        val w = BufferedWriter(FileWriter(expenses_file, false))
        w.use { out ->
            for (i in expense) {
                out.write(i.toFile() + "\n")
            }
        }
        w.close()
    }

    //Writes all earnings from earnings array list into earnings_file.
    fun writeEarnings() {
        val w = BufferedWriter(FileWriter(earnings_file, false))
        w.use { out ->
            for (i in earning) {
                out.write(i.toFile() + "\n")
            }
        }
        w.close()
    }

    //Writes the expenses sources into expenses sources file.
    fun writeExpensesSources() {
        val w = BufferedWriter(FileWriter(expenses_sources_file, false))
        w.use {
            for (i in expensesSources) {
                it.write(i + "\n")
            }
        }
    }

    //Writes the earnings sources into earnings sources file.
    fun writeEarningsSources() {
        val w = BufferedWriter(FileWriter(earnings_sources_file, false))
        w.use {
            for (i in earningsSources) {
                it.write(i + "\n")
            }
        }
    }

    //Adds expense to expenses arraylist
    fun addExpense(date: Date, category: String, price: BigDecimal, adi: String) {
        expense.add(
            searchExpenses(date, expense, 0, expense.size - 1),
            Expense(date, category, price, adi)
        )
        writeExpenses()
    }

    //Adds the earning into the earnings array list and
    // opens the earnings text file and writes everything inside the arraylist into it.
    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String) {
        earning.add(
            searchEarnings(date, earning, 0, earning.size - 1),
            Earning(date, source, amount, adi)
        )
        writeEarnings()
    }

    //Adds the expense source to the arraylist and opens
    // expenses source text file and writes everything inside expenses sources file into it.
    fun addExpensesSource(source: String) {
        expensesSources.add(searchExpensesSources(source, 0, expensesSources.size - 1), source)
        writeExpensesSources()
    }

    //opens earnings sources text file and writes
    // everything in the earnings source arraylist into it.
    fun addEarningsSource(source: String) {
        earningsSources.add(searchEarningsSources(source, 0, earningsSources.size - 1), source)
        writeEarningsSources()
    }

    /**Searches through earnings array list and returns the index of where the date should be inserted in to preserve order
     * @param [date] date to look for
     * @param [list] list to search in
     * @param [min] minimum index of list to search for
     * @param [max] maximum index of list to search for
     */

    private fun searchEarnings(date: Date, list: ArrayList<Earning>, min: Int, max: Int): Int {
        if (earning.isEmpty()) return 0
        if (date.compareTo(list[max].getDate()) == 1) {
            return list.size
        }
        val i = (max + min) / 2
        val d = list[i].getDate()
        if (max - min <= 1) {
            return if (date.compareTo(d) == 1) i + 1
            else i
        }

        return when (date.compareTo(d)) {
            1 -> searchEarnings(date, list, i, max)
            -1 -> searchEarnings(date, list, min, i)
            else -> i
        }
    }

    /**Searches through the expenses array list and returns the index of the array of where the date should go to preserve order
     * Returns:
     * i: Int
     */

    private fun searchExpenses(date: Date, list: ArrayList<Expense>, min: Int, max: Int): Int {
        if (expense.isEmpty()) return 0
        if (date.compareTo(list[max].getDate()) == 1) {
            return list.size
        }
        val i = (max + min) / 2
        val d = list[i].getDate()
        if (max - min <= 1) {
            return if (date.compareTo(d) == 1) i + 1
            else i
        }

        return when (date.compareTo(d)) {
            1 -> searchExpenses(date, list, i, max)
            -1 -> searchExpenses(date, list, min, i)
            else -> i
        }
    }

    /**Searches through the expenses sources to put the source in alphabetically to preserve order
     * Returns:
     * i: Int
     */
    private fun searchExpensesSources(source: String, min: Int, max: Int): Int {
        if (expensesSources.isEmpty()) return 0
        if (source.compareTo(expensesSources[max]) >= 1) {
            return expensesSources.size
        } else {
            val i: Int = (max + min) / 2
            val s: String = expensesSources[i]

            if (max - min <= 1) {
                return if (source.compareTo(s) >= 1) i + 1
                else i
            }

            return if (source > s) {
                searchExpensesSources(source, i, max)
            } else if (source < s) {
                searchExpensesSources(source, min, i)
            } else return i
        }
    }

    /**
     * Searches through earnings sources to put the source in alphabetically to preserve the order.
     * Returns:
     * i: Int
     */
    private fun searchEarningsSources(source: String, min: Int, max: Int): Int {
        if (earningsSources.isEmpty()) return 0
        if (source.compareTo(earningsSources[max]) >= 1) {
            return earningsSources.size
        } else {
            val i: Int = (max + min) / 2
            val s: String = earningsSources[i]

            if (max - min <= 1) {
                return if (source.compareTo(s) >= 1) i + 1
                else i
            }

            return if (source > s) {
                searchEarningsSources(source, i, max)
            } else if (source < s) {
                searchEarningsSources(source, min, i)
            } else return i
        }
    }

    //Open expenses text file to read each line and add each line of information to the expense array list.
    fun readExpenses() {
        expense.clear()
        val f = BufferedReader(FileReader(expenses_file))
        f.forEachLine {
            val text = it
            if (text.isNotBlank()) {
                val found = text.split('@')
                val date = Date(found[0])
                val source = found[1]
                val amount = BigDecimal(found[2])
                var addInfo = ""
                try {
                    addInfo = " " + found[3]
                } catch (e: Exception) {
                }
                expense.add(Expense(date, source, amount, addInfo))
            }
        }
        f.close()
    }

    //Open earnings text file to read each line and add the information to the earning array list.
    fun readEarnings() {
        earning.clear()
        val r = BufferedReader(FileReader(earnings_file))
        r.forEachLine {
            val text = it
            if (text.isNotBlank()) {
                val found = text.split('@')
                val date = Date(found[0])
                val source = found[1]
                val amount = BigDecimal(found[2])
                var addInfo = ""
                try {
                    addInfo = found[3]
                } catch (e: Exception) {
                }
                earning.add(Earning(date, source, amount, addInfo))
            }
        }
        r.close()
    }

    //Reads the expenses sources in the file.
    fun readExpensesSources() {
        expensesSources.clear()
        val r = BufferedReader(FileReader((expenses_sources_file)))
        r.forEachLine {
            expensesSources.add(it)
        }
    }

    //Reads the earnings sources in the file.
    fun readEarningsSources() {
        earningsSources.clear()
        val r = BufferedReader(FileReader(earnings_sources_file))
        r.forEachLine {
            earningsSources.add(it)
        }
    }

    fun getEarningAmountPerCategory(array: MutableList<Earning>): Array<BigDecimal> {
        val amounts = Array(getEarningsSources().size, { BigDecimal(0) })
        for (source in 0 until getEarningsSources().size) {
            for (earning in array) {
                if (earning.getSource() == (getEarningsSources()[source])) {
                    amounts[source] += amounts[source] + earning.getAmount()
                }
            }
        }
        return amounts
    }

    fun getExpenseAmountPerCategory(array: MutableList<Expense>): Array<BigDecimal> {
        val amounts = Array(getExpensesSources().size, { BigDecimal(0) })
        for (source in 0 until getExpensesSources().size) {
            for (expense in array) {
                if (expense.getSource() == (getExpensesSources()[source])) {
                    amounts[source] += amounts[source] + expense.getAmount()
                }
            }
        }
        return amounts
    }

    //returns a list of all the dates that are between the start and end date
    fun searchRangeEarnings(start: Date, end: Date): MutableList<Earning> {
        var startIndex = searchEarnings(start, earning, 0, earning.size - 1)
        var endIndex = searchEarnings(end, earning, 0, earning.size - 1)
        return earning.subList(startIndex, endIndex)
    }

    fun searchRangeExpenses(start: Date, end: Date): MutableList<Expense> {
        var startIndex = searchExpenses(start,expense,0,earning.size-1)
        var endIndex = searchExpenses(end,expense,0,earning.size-1)
        return expense.subList(startIndex, endIndex)
    }

    fun getEarningDateStrings(array: MutableList<Earning>): ArrayList<String> {
        var dates = ArrayList<String>()

        for (date in getEarningDates(array)) {
            dates.add(date.getFullDate())
        }
        return dates
    }

    fun getEarningDates(array: MutableList<Earning>): ArrayList<Date> {
        var contains = false
        var dates = ArrayList<Date>()
        for (earning in array) {
            for (date in dates) {
                if (date.getFullDate() == earning.getDate().getFullDate()) {
                    contains = true
                    break
                }
            }
            if (!contains) {
                dates.add(earning.getDate())
            }
            contains = false
        }
        return dates
    }

    fun getExpenseDateStrings(array: MutableList<Expense>): ArrayList<String> {
        var dates = ArrayList<String>()

        for (date in getExpenseDates(array)) {
            dates.add(date.getFullDate())
        }
        return dates
    }

    fun getExpenseDates(array: MutableList<Expense>): ArrayList<Date> {
        var contains = false
        var dates = ArrayList<Date>()
        for (expense in array) {
            for (date in dates) {
                if (date.getFullDate() == expense.getDate().getFullDate()) {
                    contains = true
                    break
                }
            }
            if (!contains) {
                dates.add(expense.getDate())
            }
            contains = false
        }
        return dates
    }

    fun getAmountEarnedPerDate(array: MutableList<Earning>): Array<Float> {
        var dates = getEarningDates(array)
        var amounts = Array<Float>(dates.size, { 0.toFloat() })
        for (date in 0 until dates.size) {
            for (earning in array) {
                if (earning.getDate().getFullDate() == (dates[date].getFullDate())) {
                    amounts[date] += earning.getAmount().toFloat()
                }
            }
        }
        return amounts
    }

    fun getAmountSpentPerDate(array: MutableList<Expense>): Array<Float> {
        var dates = getExpenseDates(array)
        var amounts = Array<Float>(dates.size, { 0.toFloat() })
        for (date in 0 until dates.size) {
            for (expense in array) {
                if (expense.getDate().getFullDate() == (dates[date].getFullDate())) {
                    amounts[date] += expense.getAmount().toFloat()
                }
            }
        }
        return amounts
    }

    //get all the earning sources available within a list
    fun getEarningSources(array: MutableList<Earning>): ArrayList<String>{
        var sources = ArrayList<String>(0)
        var exists = false
        for (earning in array){
            for(source in sources) {
                if (earning.getSource() == source) {
                    exists = true
                    break
                }
            }

            if (!exists){
                sources.add(earning.getSource())
            }
            exists = false
        }
        return sources
    }

    //get all the expense sources available within a list
    fun getExpenseSources(array: MutableList<Expense>): ArrayList<String>{
        var sources = ArrayList<String>(0)
        var exists = false
        for (expense in array){
            for (source in sources){
                if (expense.getSource() == source){
                    exists = true
                    break
                }
            }

            if (!exists){
                sources.add(expense.getSource())
            }
            exists = false
        }
        return sources
    }
}