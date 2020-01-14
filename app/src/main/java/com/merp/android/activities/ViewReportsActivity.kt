package com.merp.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.merp.android.ChartsPagerAdapter
import com.merp.android.Date
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_view_reports.*


class ViewReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var start = Date(intent.getIntExtra("start year", 0),
                         intent.getIntExtra("start month",0),
                         intent.getIntExtra("start day",0))

        var end = Date(intent.getIntExtra("end year", 0),
                       intent.getIntExtra("end month", 0),
                       intent.getIntExtra("end day",0))

        try{
            val charts = arrayListOf(ChartFragment("earnings", "bar", start, end),
                                     ChartFragment("earnings", "pie", start, end),
                                     ChartFragment("expenses","bar",start, end),
                                     ChartFragment("expenses","pie", start, end))
            var viewPager: ViewPager = findViewById(R.id.viewpager)
            var pagerAdapter = ChartsPagerAdapter(supportFragmentManager, charts)
            viewPager.adapter = pagerAdapter

        }catch(e: Exception){
            val t = Toast.makeText(this@ViewReportsActivity, "No earning/expense between these dates", Toast.LENGTH_SHORT)
            t.show()
        }
    }
}
