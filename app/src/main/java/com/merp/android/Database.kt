package com.merp.android

import java.io.*
import java.math.BigDecimal
import com.merp.android.Date as Date
import com.merp.android.Earning as Earning
import com.merp.android.Expense as Expense

object Database {
    private val expenses = ArrayList<Expense>()
    private val earnings = ArrayList<Earning>()
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
    * Returns the earning array list
    *
    * @return [earnings] ArrayList<Earning>
     */

    fun getEarnings(): ArrayList<Earning> {
        return earnings
    }

    /**
    * Returns the expense array list
    *
    * @return [expenses] ArrayList<Expense>
     */
    fun getExpenses(): ArrayList<Expense> {
        return expenses
    }

    /**
    * Returns the earnings sources array list
    *
    * @return: [earningsSources] ArrayList<String>
     */
    fun getEarningsSources(): ArrayList<String> {
        return earningsSources
    }

    /**
    *Returns the expense sources array list
    *
    * @return: [expensesSources] ArrayList<String>
     */
    fun getExpensesSources(): ArrayList<String> {
        return expensesSources
    }

    /**
     * Writes all expenses from expense array list into expenses_file.
     *
     */

    fun writeExpenses() {
        val w = BufferedWriter(FileWriter(expenses_file, false))
        w.use { out ->
            for (i in expenses) {
                out.write(i.toFile() + "\n")
            }
        }
        w.close()
    }

    /**
     *  Writes all earnings from earnings array list into earnings_file.
     *
     */

    fun writeEarnings() {
        val w = BufferedWriter(FileWriter(earnings_file, false))
        w.use { out ->
            for (i in earnings) {
                out.write(i.toFile() + "\n")
            }
        }
        w.close()
    }

    /**
     * Writes the expenses sources into expenses sources file.
     */
    fun writeExpensesSources() {
        val w = BufferedWriter(FileWriter(expenses_sources_file, false))
        w.use {
            for (i in expensesSources) {
                it.write(i + "\n")
            }
        }
    }

    /**
     * Writes the earnings sources into earnings sources file.
     */
    fun writeEarningsSources() {
        val w = BufferedWriter(FileWriter(earnings_sources_file, false))
        w.use {
            for (i in earningsSources) {
                it.write(i + "\n")
            }
        }
    }

    /**
     * Adds expense to expenses arraylist
     *
     * @param [date] date of the expense
     * @param [category] category of the expense
     * @param [price] price of the expense
     * @param [adi] additional information
     *
     */
    fun addExpense(date: Date, category: String, price: BigDecimal, adi: String) {
        expenses.add(
            searchExpenses(date, expenses, 0, expenses.size - 1),
            Expense(date, category, price, adi)
        )
        writeExpenses()
    }

    /**
     * Adds earning to earnings arraylist
     *
     * @param [date] date of the earning
     * @param [source] source of the earning
     * @param [amount] amount of the earning
     * @param [adi] additional information
     *
     */

    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String) {
        earnings.add(
            searchEarnings(date, earnings, 0, earnings.size - 1),
            Earning(date, source, amount, adi)
        )
        writeEarnings()
    }

    /**
     * Adds expense source to expense sources arraylist
     *
     * @param [source] source of the expense
     *
     */

    fun addExpensesSource(source: String) {
        expensesSources.add(searchExpensesSources(source, 0, expensesSources.size - 1), source)
        writeExpensesSources()
    }

    /**
     * Adds earning source to earning sources arraylist
     *
     * @param [source] source of the earning
     *
     */

    fun addEarningsSource(source: String) {
        earningsSources.add(searchEarningsSources(source, 0, earningsSources.size - 1), source)
        writeEarningsSources()
    }

    /**
     * Searches through earnings array list and returns the index of where the date should be inserted in to preserve order
     *
     * @param [date] date to look for
     * @param [list] list to search in
     * @param [min] minimum index of list to search for
     * @param [max] maximum index of list to search for
     *
     * @return average index of max and min
     *
     */

    private fun searchEarnings(date: Date, list: ArrayList<Earning>, min: Int, max: Int): Int {
        if (earnings.isEmpty()) return 0
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

    /**
     * Searches through the expenses array list and returns the index of the array of where the date should go to preserve order
     *
     * @param [date] date to look for
     * @param [list] list to search in
     * @param [min] minimum index of list to search for
     * @param [max] maximum index of list to search for
     *
     * @return average index of max and min
     *
     */

    private fun searchExpenses(date: Date, list: ArrayList<Expense>, min: Int, max: Int): Int {
        if (expenses.isEmpty()) return 0
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

    /**
     * Searches through the expenses sources to put the source in alphabetically to preserve order
     *
     * @param [source] source to search for in list
     * @param [min] minimum index to search in
     * @param [max] maximum index to search in
     *
     * @return average index of max and min
     *
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
     *
     * @param [source] source to search for
     * @param [min] minimum index to search in
     * @param [max] maximum index to search in
     *
     * @return Average index of max and min.
     *
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

    /**
     * Initializes expenses arraylist
     */

    fun readExpenses() {
        expenses.clear()
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
                expenses.add(Expense(date, source, amount, addInfo))
            }
        }
        f.close()
    }

    /**
     * Initializes earnings ayyalist
     */

    fun readEarnings() {
        earnings.clear()
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
                earnings.add(Earning(date, source, amount, addInfo))
            }
        }
        r.close()
    }

    /**
     * Initializes expenses sources arraylist
     */

    fun readExpensesSources() {
        expensesSources.clear()
        val r = BufferedReader(FileReader((expenses_sources_file)))
        r.forEachLine {
            expensesSources.add(it)
        }
    }

    /**
     * Initializes earnings sources arraylist.
     */

    fun readEarningsSources() {
        earningsSources.clear()
        val r = BufferedReader(FileReader(earnings_sources_file))
        r.forEachLine {
            earningsSources.add(it)
        }
    }

    /**
     * Returns a list of amount earned per category.
     *
     * @param [array] list of earning objects to search through.
     *
     * @return list of amount earned per category.
     *
     */

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

    /**
     * Returns a list of amount spent per category
     *
     * @param [array] list of expense objects to search through
     *
     * @return list of amount spent per category.
     *
     */
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

    /**
     * Return list of all earnings between start and end date specified
     *
     * @param [start] start date
     * @param [end] end date
     *
     * @return List of earnings with dates between start and end.
     *
     */

    fun searchRangeEarnings(start: Date, end: Date): MutableList<Earning> {
        var a = mutableListOf<Earning>()
        val startIndex = searchEarnings(start, earnings, 0, earnings.size - 1)
        val endIndex = searchEarnings(end, earnings, 0, earnings.size - 1)
        a = earnings.subList(startIndex, endIndex)
        for (e in earnings){
            if (e.getDate().getFullDate() == end.getFullDate()){
                a.add(e)
            }
        }
        return a
    }

    /**
     * Returns list of all expenses between start and end date specified
     *
     * @param [start] start date
     * @param [end] end date
     *
     * @return List of expenses with dates between start and end.
     *
     */

    fun searchRangeExpenses(start: Date, end: Date): MutableList<Expense> {

        var a = mutableListOf<Expense>()
        val startIndex = searchExpenses(start,expenses,0, expenses.size-1)
        val endIndex = searchExpenses(end,expenses,0,expenses.size-1)
        a = expenses.subList(startIndex,endIndex)
        for(e in expenses){
            if (e.getDate().getFullDate() == end.getFullDate()){
                a.add(e)
            }
        }
        return a
    }

    /**
     * Returns the earning dates within a range into string
     *
     * @param [array] list of earnings within within specific dates
     *
     * @return list of dates in string within specific list of earnings within dates
     */
    fun getEarningDateStrings(array: MutableList<Earning>): ArrayList<String> {
        val dates = ArrayList<String>()

        for (date in getEarningDates(array)) {
            dates.add(date.getFullDate())
        }
        return dates
    }

    /**
     * Returns earning dates objects within a range of earnings
     *
     * @param [array] list of earning objects within a specific date
     *
     * @return list of dates within the list of earnings within specific date
     *
     */
    fun getEarningDates(array: MutableList<Earning>): ArrayList<Date> {
        var contains = false
        val dates = ArrayList<Date>()
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

    /**
     * Returns string of dates within specific dates
     *
     * @param [array] List of expenses within specific range
     *
     * @return date in string within list of expenses with specific range
     */
    fun getExpenseDateStrings(array: MutableList<Expense>): ArrayList<String> {
        val dates = ArrayList<String>()

        for (date in getExpenseDates(array)) {
            dates.add(date.getFullDate())
        }
        return dates
    }

    /**
     * Returns list of date objects within specific range of expense objects
     *
     * @param [array] list of expenses within specific range
     *
     * @return list of date objects within specific range of expense objects
     *
     */
    fun getExpenseDates(array: MutableList<Expense>): ArrayList<Date> {
        var contains = false
        val dates = ArrayList<Date>()
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

    /**
     * Returns amount earned per day
     *
     * @param [array] list of earning objects within specific range of dates
     *
     * @return amount earned per day within specific range
     *
     */
    fun getAmountEarnedPerDate(array: MutableList<Earning>): Array<Float> {
        val dates = getEarningDates(array)
        val amounts = Array<Float>(dates.size, { 0.toFloat() })
        for (date in 0 until dates.size) {
            for (earning in array) {
                if (earning.getDate().getFullDate() == (dates[date].getFullDate())) {
                    amounts[date] += earning.getAmount().toFloat()
                }
            }
        }
        return amounts
    }

    /**
     *
     * Returns amount spent per day
     *
     * @param [array] List of expense objects within specific dates
     *
     * @return Amount spent per date within specified range
     *
     */
    fun getAmountSpentPerDate(array: MutableList<Expense>): Array<Float> {
        val dates = getExpenseDates(array)
        val amounts = Array<Float>(dates.size, { 0.toFloat() })
        for (date in 0 until dates.size) {
            for (expense in array) {
                if (expense.getDate().getFullDate() == (dates[date].getFullDate())) {
                    amounts[date] += expense.getAmount().toFloat()
                }
            }
        }
        return amounts
    }

    /**
     * Returns the sources of earnings within a specific date
     *
     * @param [array] List of earnings within a specific date
     *
     * @return all earning sources in list of specific earnings
     *
     */
    fun getEarningSources(array: MutableList<Earning>): ArrayList<String>{
        val sources = ArrayList<String>(0)
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

    /**
     * Returns expense sources within specific range
     *
     * @param [array] List of expenses within specific range
     *
     * @return All expense sources within specific range
     *
     */
    fun getExpenseSources(array: MutableList<Expense>): ArrayList<String>{
        val sources = ArrayList<String>(0)
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