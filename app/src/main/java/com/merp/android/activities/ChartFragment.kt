package com.merp.android.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.merp.android.*
import com.merp.android.Database.getAmountEarnedPerDate
import com.merp.android.Database.getAmountSpentPerDate
import com.merp.android.Database.getEarningDates
import com.merp.android.Database.getEarnings
import com.merp.android.Database.getExpenseDates
import com.merp.android.Database.getExpenses
import com.merp.android.Database.searchRangeEarnings
import com.merp.android.Database.searchRangeExpenses


/**
 * A simple [Fragment] subclass.
 */
class ChartFragment(entryType: String, chartType: String, start: Date, end: Date) : Fragment() {
    private lateinit var mBarChart:BarChart
    private lateinit var mPieChart:PieChart
    private var entry =entryType
    private var m = chartType
    private var s = start
    private var e = end

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chart, container, false)

        mBarChart = view.findViewById(R.id.barChart)
        mPieChart = view.findViewById(R.id.pieChart)
        getChart(entry,m, s, e)
        return view
    }

    //gets the information required to build the graph. Builds either a line graph or pie chart for expense or earning
    private fun getChart(entry: String?, method: String?, start: Date, end: Date){
        var yAxis:Array<Float>
        var tempXAxisLabels = ArrayList<Date>()
        if (method.equals("pie")){
            val pieEntries = ArrayList<PieEntry>()
            var label = ""
            var desc = ""

            if (entry.equals("earnings")){ //adding y (earning amount) and x (earning category) as a sector in the pie chart
                val values = Database.getEarningAmountPerCategory(searchRangeEarnings(start, end))
                val categories = Database.getEarningsSources()

                for (i in 0 until categories.size){
                    pieEntries.add(PieEntry(values[i].toFloat(), categories[i]))
                }

                label = "Amount earned"
                desc = "Amount earned per source"

            }else if (entry.equals("expenses")){
                val values = Database.getExpenseAmountPerCategory(searchRangeExpenses(start, end))
                val categories = Database.getExpensesSources()

                for (i in 0 until categories.size){
                    pieEntries.add(PieEntry(values[i].toFloat(), categories[i]))
                }

                label = "Amount spent"
                desc = "Amount spent per category"
            }

            mPieChart.visibility = View.VISIBLE
            mPieChart.animateXY(1000,1000)
            val pieDataSet = PieDataSet(pieEntries, label)

            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS.toMutableList())
            val pieData =  PieData(pieDataSet)

            mPieChart.data = pieData

            val description = Description()
            description.text = desc
            mPieChart.description = description
            mPieChart.invalidate()

        }else if(method.equals("bar")){
            var label = ""
            var desc = ""
            val barEntries = ArrayList<BarEntry>()

            if (entry.equals("earnings")){
                val arrayInRange = searchRangeEarnings(start, end)
                yAxis = getAmountEarnedPerDate(arrayInRange)
                for (earning in 0 until yAxis.size){
                    barEntries.add(BarEntry(earning.toFloat(),
                       yAxis[earning]))
                }
                label = "Amount earned"
                desc = "Amount earned per date"
                tempXAxisLabels = getEarningDates(arrayInRange)

            }else if(entry.equals("expenses")){
                val arrayInRange = searchRangeExpenses(start, end)
                yAxis = getAmountSpentPerDate(arrayInRange)
                for (expense in 0 until yAxis.size){
                    barEntries.add(BarEntry(expense.toFloat(),
                       yAxis[expense]))
                }
                label = "Amount spent"
                desc = "Amount spent per date"
                tempXAxisLabels = getExpenseDates(arrayInRange)
            }

            mBarChart.visibility = View.VISIBLE

            mBarChart.animateXY(1000,1000)
            val barDataSet = BarDataSet(barEntries,label)
            barDataSet.valueTextSize = 20f
            var xAxisLabels = ArrayList<String>()

            for (label in tempXAxisLabels){
                xAxisLabels.add(label.getFullDate())
            }

            var xAxis = mBarChart.xAxis
            xAxis.textSize = 12f
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            if(entry=="earnings"){
                xAxis.valueFormatter = EarningsFormatter()
            }else if(entry=="expenses"){
                xAxis.valueFormatter = ExpensesFormatter()
            }

            xAxis.granularity = 1f

            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS.toMutableList())
            val barData = BarData(barDataSet)
            barData.setBarWidth(0.9f)

            mBarChart.data = barData

            val description = Description()
            description.text = desc
            mBarChart.description = description

            mBarChart.setVisibleXRangeMaximum(4f)
            mBarChart.setVisibleXRangeMinimum(4f)
            mBarChart.invalidate()
        }
    }
}
