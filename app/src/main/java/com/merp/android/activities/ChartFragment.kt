package com.merp.android.activities

import android.os.Bundle
import android.util.Log
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
import com.merp.android.*
import com.merp.android.Database.getAmountEarnedPerDate
import com.merp.android.Database.getAmountSpentPerDate
import com.merp.android.Database.getEarningDates
import com.merp.android.Database.getEarnings
import com.merp.android.Database.getExpenseDates
import com.merp.android.Database.getExpenses
import com.merp.android.Database.searchRangeEarnings
import com.merp.android.Database.searchRangeExpenses
import java.math.BigDecimal


/**
 * The Kotlin class for the fragment used for creating bar graphs and pie charts in [ViewReportsActivity].
 */
class ChartFragment(entryType: String, chartType: String, start: Date, end: Date) : Fragment() {
    /** A bar graph */
    private lateinit var mBarChart: BarChart

    /** A pie chart */
    private lateinit var mPieChart: PieChart

    /** Records whether the chart displays [Earning]s or [Expense]s */
    private var entry = entryType

    /** Records which chart will be displayed (bar or pie) */
    private var m = chartType

    /** Records the selected start date */
    private var s = start

    /** Records the selected end date */
    private var e = end

    /**
     * Inflates the layout for this fragment and initializes [mBarChart] and [mPieChart].
     */
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

    /**
     * Gets the information required for the graph, then creates the specified graph.
     */
    private fun getChart(entry: String?, method: String?, start: Date, end: Date){
        val yAxis:Array<BigDecimal>
        if (method.equals("pie")){ //if creating a pie chart
            val pieEntries = ArrayList<PieEntry>()
            var label = ""
            var desc = ""

            if (entry.equals("earnings")){ //adding y (earning amount) and x (earning category) as a sector in the pie chart
                val arrayInRange = searchRangeEarnings(start, end)
                val values = Database.getEarningAmountPerCategory(arrayInRange)
                val categories = Database.getEarningSources(arrayInRange)

                for (i in 0 until categories.size){
                    pieEntries.add(PieEntry(values[i].toFloat(), categories[i]))
                }

                label = "Source"
                desc = "Amount earned per source"

            }else if (entry.equals("expenses")){
                val arrayInRange = searchRangeExpenses(start, end)
                val values = Database.getExpenseAmountPerCategory(arrayInRange)
                val categories = Database.getExpenseSources(arrayInRange)

                for (i in 0 until categories.size){
                    pieEntries.add(PieEntry(values[i].toFloat(), categories[i]))
                }

                label = "Source"
                desc = "Amount spent per source"
            }

            mPieChart.visibility = View.VISIBLE
            mPieChart.animateXY(1000,1000)
            val pieDataSet = PieDataSet(pieEntries, label)

            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS.toMutableList())
            val pieData =  PieData(pieDataSet)

            pieData.setValueTextSize(25f)
            mPieChart.data = pieData

            val description = Description()
            description.text = desc
            mPieChart.description = description
            mPieChart.invalidate()

        }else if(method.equals("bar")){ //if creating a bar graph
            var label = ""
            var desc = ""
            val barEntries = ArrayList<BarEntry>()

            if (entry.equals("earnings")){
                val arrayInRange = searchRangeEarnings(start, end)
                yAxis = getAmountEarnedPerDate(arrayInRange)
                for (earning in 0 until yAxis.size){
                    barEntries.add(BarEntry(earning.toFloat(),
                       yAxis[earning].toFloat()))
                }
                label = "Date"
                desc = "Amount earned per date"

            }else if(entry.equals("expenses")){
                val arrayInRange = searchRangeExpenses(start, end)
                yAxis = getAmountSpentPerDate(arrayInRange)
                for (expense in 0 until yAxis.size){
                    barEntries.add(BarEntry(expense.toFloat(),
                       yAxis[expense].toFloat()))
                }
                label = "Date"
                desc = "Amount spent per date"
            }

            mBarChart.visibility = View.VISIBLE

            mBarChart.animateXY(1000,1000)
            val barDataSet = BarDataSet(barEntries,label)
            barDataSet.valueTextSize = 20f

            val xAxis = mBarChart.xAxis
            xAxis.textSize = 12f
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            if(entry=="earnings"){
                xAxis.valueFormatter = EarningsFormatter(start, end)
            }else if(entry=="expenses"){
                xAxis.valueFormatter = ExpensesFormatter(start, end)
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
