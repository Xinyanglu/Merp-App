package com.merp.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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



        //Sets the start date variables
        val startYear = intent.getIntExtra("start year", 0)
        val startMonth = intent.getIntExtra("start month", 0)
        val startDay = intent.getIntExtra("start day", 0)

        //sets the end date variables
        val endYear = intent.getIntExtra("end year", 0)
        val endMonth = intent.getIntExtra("end month", 0)
        val endDay = intent.getIntExtra("end day", 0)

        this.title = resources.getString(R.string.title_activity_view_reports, "$startYear-$startMonth-$startDay", "$endYear-$endMonth-$endDay")

        //initializes start date and end date objects
        val start = Date(startYear, startMonth, startDay)
        val end = Date(endYear, endMonth, endDay)

        //sets the viewpager with the 4 graphs and charts needed
        val charts = arrayListOf(ChartFragment("earnings", "bar", start, end),
            ChartFragment("earnings", "pie", start, end),
            ChartFragment("expenses","bar",start, end),
            ChartFragment("expenses","pie", start, end))

        val viewPager: NonswipeableViewPager = findViewById(R.id.viewpager)
        val pagerAdapter = ChartsPagerAdapter(supportFragmentManager, charts)
        viewPager.adapter = pagerAdapter

        //sets up tabs for the view pager
        val tabs = findViewById<TabLayout>(R.id.tabLayout)
        tabs.setupWithViewPager(viewPager)

        //analysis for total earned/lost and net gain, and average gained per day
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
            stringNet = "\$$netIncome"
        }

        var averagePerDay = ((totalGained - totalLost)/daysInBetween)
        val stringAverage: String
        if(averagePerDay.toInt() < 0){
            averagePerDay *= BigDecimal(-1)
            stringAverage = "-\$$averagePerDay"
        }else{
            stringAverage = "\$$averagePerDay"
        }

        analysis.text = getString(R.string.text_analysis, stringNet, "\$$totalGained", "\$$totalLost", stringAverage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view_reports, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_display_help){ //navigate to HelpActivity with instructions for this activity
            val data = Intent(this, HelpActivity::class.java).apply{
                putExtra("source", "ViewReportsActivity")
            }
            startActivity(data)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
