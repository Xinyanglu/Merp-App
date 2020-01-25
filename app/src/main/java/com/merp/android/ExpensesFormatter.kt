package com.merp.android

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.merp.android.Database.searchRangeExpenses

/**
 * A modified version of the ValueFormatter class from the MPAndroidChart API.
 * Sets up the x-axis for the [Expense]s bar graph.
 *
 * @param [start] The start date
 * @param [end] The end date
 */
class ExpensesFormatter(start: Date, end: Date): ValueFormatter(){

    /** All [Date]s between the user-specified date range */
    private var dates = Database.getExpenseDateStrings(searchRangeExpenses(start, end))

    /**
     * Sets up the x-axis for the expenses bar graph using the dates from the user-specified date range.
     */
    override fun getAxisLabel(value: Float, axis: AxisBase?): String{
        return dates.getOrNull(value.toInt()) ?: value.toString()
    }
}