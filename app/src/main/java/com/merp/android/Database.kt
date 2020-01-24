package com.merp.android

import java.io.*
import java.math.BigDecimal
import com.merp.android.Date as Date
import com.merp.android.Earning as Earning
import com.merp.android.Expense as Expense

object Database {
    /** An ArrayList for recording all [Expense] objects */
    private val expenses = ArrayList<Expense>()

    /** An ArrayList for recording all [Earning] objects */
    private val earnings = ArrayList<Earning>()

    /** An ArrayList for recording all sources of [Expense]s */
    private val expensesSources = ArrayList<String>()

    /** An ArrayList for recording all sources of [Earning]s */
    private val earningsSources = ArrayList<String>()

    /** The file directory of the text file for saving [Earning]s */
    private var earnings_file = ""

    /** The file directory of the text file for saving [Expense]s */
    private var expenses_file = ""

    /** The file directory of the text file for saving sources of [Earning]s */
    private var earnings_sources_file = ""

    /** The file directory of the text file for saving sources of [Expense]s */
    private var expenses_sources_file = ""

    /**
     * Sets the file directories.
     *
     * @param [earningsDir] The file directory for [earnings_file]
     * @param [expensesDir] The file directory for [expenses_file]
     * @param [earnSourcesDir] The file directory for [earnings_sources_file]
     * @param [expSourcesDir] The file directory for [expenses_sources_file]
     */
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
    * Returns the [earnings] ArrayList
    *
    * @return [earnings]
     */

    fun getEarnings(): ArrayList<Earning> {
        return earnings
    }

    /**
    * Returns the [expenses] ArrayList
    *
    * @return [expenses]
     */
    fun getExpenses(): ArrayList<Expense> {
        return expenses
    }

    /**
    * Returns the [earningsSources] ArrayList
    *
    * @return [earningsSources]
     */
    fun getEarningsSources(): ArrayList<String> {
        return earningsSources
    }

    /**
     * Returns the [expensesSources] ArrayList
     *
     * @return [expensesSources]
     */

    fun getExpensesSources(): ArrayList<String> {
        return expensesSources
    }

    /**
     * Writes all [Expense]s from [expenses] ArrayList into [expenses_file].
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
     *  Writes all [Earning]s from [earnings] ArrayList into [earnings_file].
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
     * Writes the [Expense.source]s from [expensesSources] ArrayList into [expenses_sources_file].
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
     * Writes the [Earning.source]s from [earningsSources] ArrayList into [earnings_sources_file].
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
     * Adds an [Expense] to the [expenses] ArrayList.
     *
     * @param [date] The [Date] of the [Expense]
     * @param [source] The source of the [Expense]
     * @param [amount] The amount spent
     * @param [adi] Additional information
     */
    fun addExpense(date: Date, source: String, amount: BigDecimal, adi: String) {
        expenses.add(
            searchExpenses(date, expenses),
            Expense(date, source, amount, adi)
        )
        writeExpenses()
    }

    /**
     * Adds an [Earning] to the [expenses] ArrayList.
     *
     * @param [date] The [Date] of the [Earning]
     * @param [source] The source of the [Earning]
     * @param [amount] The amount earned
     * @param [adi] Additional information
     */

    fun addEarning(date: Date, source: String, amount: BigDecimal, adi: String) {
        earnings.add(
            searchEarnings(date, earnings),
            Earning(date, source, amount, adi)
        )
        writeEarnings()
    }

    /**
     * Adds a source of [Expense] to the [expensesSources] ArrayList.
     *
     * @param [source] The new source of expense
     */

    fun addExpensesSource(source: String) {
        expensesSources.add(searchExpensesSources(source, 0, expensesSources.size - 1), source)
        writeExpensesSources()
    }

    /**
     * Adds a source of [Earning] to the [earningsSources] ArrayList.
     *
     * @param [source] The new source of earning
     *
     */

    fun addEarningsSource(source: String) {
        earningsSources.add(searchEarningsSources(source, 0, earningsSources.size - 1), source)
        writeEarningsSources()
    }

    /**
     * Searches through the [earnings] ArrayList and returns the index of where the new [Earning]
     * should be added to keep the [earnings] ArrayList sorted by date.
     *
     * @param [date] The date of the earning that will be added
     * @param [list] The ArrayList to search in
     *
     * @return Where the new [Earning] should be added
     *
     */

    private fun searchEarnings(date: Date, list: ArrayList<Earning>): Int {
        if(list.isEmpty()) return 0
        if(date > list[list.size-1].getDate()){
            return list.size
        }
        for(pos in 0 until list.size){
            if(date.compareTo(list[pos].getDate()) <= 0){
                return pos
            }
        }
        return 0
    }

    /**
     * Searches through the [expenses] ArrayList and returns the index of the where the new [Expense]
     * should be added to keep the [expenses] ArrayList sorted by date.
     *
     * @param [date] The date of the expense that will be added
     * @param [list] The ArrayList to search in
     *
     * @return Where the new [Expense] should be added
     *
     */

    private fun searchExpenses(date: Date, list: ArrayList<Expense>): Int {
        if(list.isEmpty()) return 0
        if(date > list[list.size-1].getDate()){
            return list.size
        }
        for(pos in 0 until list.size){
            if(date.compareTo(list[pos].getDate()) <= 0){
                return pos
            }
        }
        return 0
    }

    /**
     * Searches through the [expensesSources] ArrayList and returns the index of where the new
     * source should be added to keep the [expensesSources] ArrayList sorted alphabetically.
     *
     * @param [source] The name of the source that will be added
     * @param [min] The minimum index of the [expensesSources] ArrayList to search in
     * @param [max] The maximum index of the [expensesSources] ArrayList to search in
     *
     * @return Where the new source of expense should be added
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
     * Searches through the [earningsSources] ArrayList and returns the index of where the new
     * source should be added to keep the [earningsSources] ArrayList sorted alphabetically.
     *
     * @param [source] The name of the source that will be added
     * @param [min] The minimum index of the [earningsSources] ArrayList to search in
     * @param [max] The maximum index of the [earningsSources] ArrayList to search in
     *
     * @return Where the new source of earning should be added
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
     * Reads the text file of [Expense]s into the [expenses] ArrayList.
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
     * Reads the text file of [Earning]s into the [earnings] ArrayList.
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
     * Reads the text file of sources of expenses into the [expensesSources] ArrayList.
     */

    fun readExpensesSources() {
        expensesSources.clear()
        val r = BufferedReader(FileReader((expenses_sources_file)))
        r.forEachLine {
            expensesSources.add(it)
        }
    }

    /**
     * Reads the text file of sources of earnings into the [earningsSources] ArrayList.
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
        val amounts = Array(getEarningSources(array).size, { BigDecimal(0) })
        for (source in 0 until getEarningSources(array).size) {
            for (earning in array) {
                if (earning.getSource() == (getEarningSources(array)[source])) {
                    amounts[source] +=  earning.getAmount()
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
        val amounts = Array(getExpenseSources(array).size, { BigDecimal(0) })
        for (source in 0 until getExpenseSources(array).size) {
            for (expense in array) {
                if (expense.getSource() == (getExpenseSources(array)[source])) {
                    amounts[source] += expense.getAmount()
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
        val startIndex = searchEarnings(start, earnings)
        val endIndex = searchEarnings(end, earnings)
        val a = earnings.subList(startIndex, endIndex)
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
        val startIndex = searchExpenses(start,expenses)
        val endIndex = searchExpenses(end,expenses)
        val a = expenses.subList(startIndex,endIndex)
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

    fun getAmountEarnedPerDate(array: MutableList<Earning>): Array<BigDecimal> {
        val dates = getEarningDates(array)
        val amounts = Array<BigDecimal>(dates.size, { BigDecimal(0) })
        for (date in 0 until dates.size) {
            for (earning in array) {
                if (earning.getDate().getFullDate() == dates[date].getFullDate()) {
                    amounts[date] += earning.getAmount()
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

    fun getAmountSpentPerDate(array: MutableList<Expense>): Array<BigDecimal> {
        val dates = getExpenseDates(array)
        val amounts = Array<BigDecimal>(dates.size, { BigDecimal(0) })
        for (date in 0 until dates.size) {
            for (expense in array) {
                if (expense.getDate().getFullDate() == dates[date].getFullDate()) {
                    amounts[date] += expense.getAmount()
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

    /**
     * Returns the total amount earned within a previously specified date range.
     *
     * @param [array] A list of [Earning]s within a specific date range
     */
    fun totalEarned(array: MutableList<Earning>): BigDecimal{
        var total = BigDecimal(0)
        for(earning in array){
            total += earning.getAmount()
        }

        return total
    }

    /**
     * Returns the total amount spent within a previously specified date range.
     *
     * @param [array] A list of [Expense]s within a specific date range
     */
    fun totalSpent(array: MutableList<Expense>): BigDecimal{
        var total = BigDecimal(0)
        for(expense in array){
            total += expense.getAmount()
        }

        return total
    }
}