package com.merp.android

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.merp.android.Database.getExpenses

class ExpensesFormatter: ValueFormatter(){
    private var dates = Database.getExpenseDateStrings(getExpenses())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String{
        return dates.getOrNull(value.toInt()) ?: value.toString()
    }

}