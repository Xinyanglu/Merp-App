package com.merp.android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.merp.android.*
import kotlinx.android.synthetic.main.activity_view_reports.*
import kotlinx.android.synthetic.main.content_view_reports.*
import java.math.BigDecimal


class ViewReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO: calculate the true value of these variables. See the text_analysis string.xml resource for adding more info to the TextView.
        //in the resource, there are things like "%1$d"
        //%x where x is an integer represents the order at which the values will be entered in getString(R.string.text_analysis, %1, %2, %3)
        //$d represents that it will be an integer, $s is string (i think), dunno the rest

        val startYear = intent.getIntExtra("start year", 0)
        val startMonth = intent.getIntExtra("start month", 0)
        val startDay = intent.getIntExtra("start day", 0)

        val endYear = intent.getIntExtra("end year", 0)
        val endMonth = intent.getIntExtra("end month", 0)
        val endDay = intent.getIntExtra("end day", 0)

        this.title = resources.getString(R.string.title_activity_view_reports, "$startYear-$startMonth-$startDay", "$endYear-$endMonth-$endDay")

        val start = Date(startYear, startMonth, startDay)
        val end = Date(endYear, endMonth, endDay)

        val charts = arrayListOf(ChartFragment("earnings", "bar", start, end),
            ChartFragment("earnings", "pie", start, end),
            ChartFragment("expenses","bar",start, end),
            ChartFragment("expenses","pie", start, end))

        val viewPager: NonswipeableViewPager = findViewById(R.id.viewpager)
        val pagerAdapter = ChartsPagerAdapter(supportFragmentManager, charts)
        viewPager.adapter = pagerAdapter

        val tabs = findViewById<TabLayout>(R.id.tabLayout)
        tabs.setupWithViewPager(viewPager)

        val rangeEarnings = Database.searchRangeEarnings(start,end)
        val rangeExpenses = Database.searchRangeExpenses(start,end)

        val totalGained = Database.totalEarned(rangeEarnings)
        val totalLost = Database.totalSpent(rangeExpenses)
        val daysInBetween = BigDecimal(start.daysUntil(end))

        var netIncome = (totalGained - totalLost)
        val stringNet: String
        if(netIncome.toInt() < 0){
            netIncome *= BigDecimal(-1)
            stringNet = "-\$$netIncome"
        }else{
            stringNet = netIncome.toString()
        }

        var averagePerDay = ((totalGained - totalLost)/daysInBetween)
        val stringAverage: String
        if(averagePerDay.toInt() < 0){
            averagePerDay *= BigDecimal(-1)
            stringAverage = "-\$$averagePerDay"
        }else{
            stringAverage = averagePerDay.toString()
        }

        analysis.text = getString(R.string.text_analysis, stringNet, "$totalGained", "$totalLost", stringAverage)
    }
}
