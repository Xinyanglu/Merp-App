package com.merp.android.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.merp.android.Database
import com.merp.android.R

/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : Fragment() {
    private lateinit var mLineChart:LineChart
    private lateinit var mPieChart:PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chart, container, false)

        mLineChart = view.findViewById(R.id.lineChart)
        mPieChart = view.findViewById(R.id.pieChart)
        getChart(arguments?.getString("entry"), arguments?.getString("method"))

        return view
    }

    //gets the information required to build the graph. Builds either a line graph or pie chart for expense or earning
    private fun getChart(entry: String?, method: String?){
        if (method.equals("pie")){
            val pieEntries = ArrayList<PieEntry>()
            var label = ""
            var desc = ""

            if (entry.equals("earnings")){ //adding y (earning amount) and x (earning category) as a sector in the pie chart
                val values = Database.getEarningAmountPerCategory()
                val categories = Database.getEarningsSources()

                for (i in 0 until categories.size){
                    pieEntries.add(PieEntry(values[i].toFloat(), categories[i]))
                }

                label = "Amount earned"
                desc = "Amount earned per source"

            }else if (entry.equals("expenses")){
                val values = Database.getExpenseAmountPerCategory()
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

        }else if(method.equals("line")){

            if (entry.equals("earnings")){


            }else if(entry.equals("expenses")){

            }
        }
    }


}
