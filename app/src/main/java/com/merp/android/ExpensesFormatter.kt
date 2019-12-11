package com.merp.android

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class ExpensesFormatter: ValueFormatter(){
    private var dates = Database.getExpenseDateStrings()

    override fun getAxisLabel(value: Float, axis: AxisBase?): String{
        return dates.getOrNull(value.toInt()) ?: value.toString()
    }

}