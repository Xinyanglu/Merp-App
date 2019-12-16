package com.merp.android

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.merp.android.Database.getEarnings

class EarningsFormatter: ValueFormatter(){
    private var dates = Database.getEarningDateStrings(getEarnings())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String{
        return dates.getOrNull(value.toInt()) ?:value.toString()
    }

}