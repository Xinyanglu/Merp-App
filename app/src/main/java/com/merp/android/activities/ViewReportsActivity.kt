package com.merp.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import com.merp.android.ChartsPagerAdapter
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_view_reports.*
import kotlinx.android.synthetic.main.content_view_reports.*


class ViewReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val charts = arrayListOf(ChartFragment("earnings", "bar"),
                                 ChartFragment("earnings", "pie"),
                                 ChartFragment("expenses","bar"),
                                 ChartFragment("expenses","pie")
        )

        var viewPager: ViewPager = findViewById(R.id.viewpager)
        var pagerAdapter = ChartsPagerAdapter(supportFragmentManager, charts)
        viewPager.adapter = pagerAdapter
    }
}
