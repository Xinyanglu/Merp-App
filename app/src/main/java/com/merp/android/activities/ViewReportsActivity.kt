package com.merp.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.merp.android.*
import kotlinx.android.synthetic.main.activity_view_reports.*
import kotlinx.android.synthetic.main.content_view_reports.*


class ViewReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tabs = findViewById<TabLayout>(R.id.tabLayout)
        tabs.setupWithViewPager(viewpager)

        //TODO: calculate the true value of these variables. See the text_analysis string.xml resource for adding more info to the TextView.
        //in the resource, there are things like "%1$d"
        //%x where x is an integer represents the order at which the values will be entered in getString(R.string.text_analysis, %1, %2, %3)
        //$d represents that it will be an integer, $s is string (i think), dunno the rest
        val netIncome = 500
        val totalGained = 750
        val totalLost = 250
        analysis.text = getString(R.string.text_analysis, netIncome, totalGained, totalLost)

        val start = Date(intent.getIntExtra("start year", 0),
                         intent.getIntExtra("start month",0),
                         intent.getIntExtra("start day",0))

        val end = Date(intent.getIntExtra("end year", 0),
                       intent.getIntExtra("end month", 0),
                       intent.getIntExtra("end day",0))

        try{
            val charts = arrayListOf(ChartFragment("earnings", "bar", start, end),
                                     ChartFragment("earnings", "pie", start, end),
                                     ChartFragment("expenses","bar",start, end),
                                     ChartFragment("expenses","pie", start, end))
            val viewPager: NonswipeableViewPager = findViewById(R.id.viewpager)
            val pagerAdapter = ChartsPagerAdapter(supportFragmentManager, charts)
            viewPager.adapter = pagerAdapter

        }catch(e: Exception){
            val t = Toast.makeText(this@ViewReportsActivity, "No earning/expense between these dates", Toast.LENGTH_SHORT)
            t.show()
        }
    }
}
