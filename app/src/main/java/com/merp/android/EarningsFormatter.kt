package com.merp.android

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.merp.android.Database.searchRangeEarnings

/**
 * A modified version of the ValueFormatter class from the MPAndroidChart API.
 * Sets up the x-axis for the [Earning]s bar graph.
 *
 * @param [start] The start date
 * @param [end] The end date
 */
class EarningsFormatter(start: Date, end: Date): ValueFormatter(){

    /** All [Date]s between the user-specified date range */
    private var dates = Database.getEarningDateStrings(searchRangeEarnings(start, end))

    /**
     * Sets up the x-axis for the earnings bar graph using the dates from the user-specified date range.
     */
    override fun getAxisLabel(value: Float, axis: AxisBase?): String{
        return dates.getOrNull(value.toInt()) ?:value.toString()
    }
}