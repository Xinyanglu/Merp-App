package com.merp.android

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.merp.android.Database.getExpenses
import com.merp.android.Database.searchRangeExpenses

class ExpensesFormatter(start: Date, end: Date): ValueFormatter(){
    private var dates = Database.getExpenseDateStrings(searchRangeExpenses(start, end))

    override fun getAxisLabel(value: Float, axis: AxisBase?): String{
        return dates.getOrNull(value.toInt()) ?: value.toString()
    }

}