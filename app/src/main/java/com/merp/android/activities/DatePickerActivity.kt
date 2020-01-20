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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)
        setSupportActionBar(toolbar)

        val start = findViewById<CalendarView>(R.id.startDate)
        val end = findViewById<CalendarView>(R.id.endDate)


        //initialize variables with values of current date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date()).split("-")

        var startYear = currentDate[0].toInt()
        var startMonth = currentDate[1].toInt()
        var startDay = currentDate[2].toInt()

        var endYear = startYear
        var endMonth = startMonth
        var endDay = startDay

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
}
