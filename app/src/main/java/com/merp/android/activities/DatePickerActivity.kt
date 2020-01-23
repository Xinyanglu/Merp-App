package com.merp.android.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.Date
import com.merp.android.R

import android.widget.Toast
import kotlinx.android.synthetic.main.activity_date_picker.*
import kotlinx.android.synthetic.main.content_date_picker.*
import java.util.*


class DatePickerActivity : AppCompatActivity() {
    private var startYear = 0
    private var startMonth = 0
    private var startDay = 0
    private var endYear = 0
    private var endMonth = 0
    private var endDay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val start = findViewById<CalendarView>(R.id.startDate)
        val end = findViewById<CalendarView>(R.id.endDate)

        btnToday.setOnClickListener{
            start.date = Date().time
            end.date = Date().time
            setDates(start.date, end.date)
        }

        btnToday.performClick()

        btnAll.setOnClickListener{
            start.date = start.minDate
            end.date = end.maxDate
            setDates(start.date, end.date)
        }

        start.setOnDateChangeListener { view, year, month, dayOfMonth ->
            startYear = year
            startMonth = month+1
            startDay = dayOfMonth
            Log.d("turnOffYourVPN", "$startDay")
        }

        end.setOnDateChangeListener{view, year, month, dayOfMonth ->
            endYear = year
            endMonth = month+1
            endDay = dayOfMonth
        }

        Log.d("startDate", "$startYear-$startMonth-$startDay")
        Log.d("endDate", "$endYear-$endMonth-$endDay")

        displayCharts.setOnClickListener {

            if (Date(startYear, startMonth, startDay) <= Date(endYear, endMonth, endDay)) {

                val data = Intent(this, ViewReportsActivity::class.java).apply {
                    putExtra("start year", startYear)
                    putExtra("start month", startMonth)
                    putExtra("start day", startDay)
                    putExtra("end year", endYear)
                    putExtra("end month", endMonth)
                    putExtra("end day", endDay)
                }

                startActivity(data)

            }
        }
    }

    private fun setDates(start: Long, end: Long){
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val startDate = sdf.format(Date(start)).split("-")
        startYear = startDate[0].toInt()
        startMonth = startDate[1].toInt()
        startDay = startDate[2].toInt()

        val endDate = sdf.format(Date(end)).split("-")
        endYear = endDate[0].toInt()
        endMonth = endDate[1].toInt()
        endDay = endDate[2].toInt()
    }
}
