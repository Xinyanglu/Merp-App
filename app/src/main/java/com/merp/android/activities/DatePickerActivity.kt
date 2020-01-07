package com.merp.android.activities

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.DatePicker
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.Date
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_date_picker.*
import kotlinx.android.synthetic.main.content_date_picker.*
import android.widget.Toast
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class DatePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)
        setSupportActionBar(toolbar)

        var start = findViewById<CalendarView>(R.id.startDate)
        var end = findViewById<CalendarView>(R.id.endDate)

        var startYear = 0
        var startMonth = 0
        var startDay = 0

        var endYear = 0
        var endMonth = 0
        var endDay = 0

        start.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            startYear = year
            startMonth = month
            startDay = dayOfMonth
        })

        end.setOnDateChangeListener(CalendarView.OnDateChangeListener{
            view, year, month, dayOfMonth ->
            endYear = year
            endMonth = month
            endDay = dayOfMonth
        })

        displayCharts.setOnClickListener {
            if (Date(startYear, startMonth, startDay) <= Date(endYear, endMonth, endDay)) {

                var data = Intent(this, ViewReportsActivity::class.java).apply {
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
}
