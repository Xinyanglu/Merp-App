package com.merp.android

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.merp.android.Database.getEarnings
import com.merp.android.Database.searchRangeEarnings

class EarningsFormatter(start: Date, end: Date): ValueFormatter(){

    private var dates = Database.getEarningDateStrings(searchRangeEarnings(start, end))

    //sets up the X axis for the earnings bar graph using the dates from user specified date range
    override fun getAxisLabel(value: Float, axis: AxisBase?): String{
        return dates.getOrNull(value.toInt()) ?:value.toString()
    }

}