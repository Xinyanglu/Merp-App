package com.merp.android.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.Date
import com.merp.android.R

import android.widget.Toast
import kotlinx.android.synthetic.main.activity_date_picker.*
import kotlinx.android.synthetic.main.content_date_picker.*
import java.util.*

/**
 * An activity for specifying the date range for which the report will display.
 * The user can use two [CalendarView]s to pick the start and end dates.
 * They can then navigate to [ViewReportsActivity] to view the generated report.
 */
class DatePickerActivity : AppCompatActivity() {
    /** Records the year selected on the start [CalendarView] */
    private var startYear = 0

    /** Records the month selected on the start [CalendarView] */
    private var startMonth = 0

    /** Records the day selected on the start [CalendarView] */
    private var startDay = 0

    /** Records the year selected on the end [CalendarView] */
    private var endYear = 0

    /** Records the month selected on the end [CalendarView] */
    private var endMonth = 0

    /** Records the day selected on the end [CalendarView] */
    private var endDay = 0

    /**
     * Sets up the layout and onClickListeners for all the components.
     *
     * Automatically called when the activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //sets up calendar views, one for start date and one for end date
        val start = findViewById<CalendarView>(R.id.startDate)
        val end = findViewById<CalendarView>(R.id.endDate)

        //sets up the button to select today's date
        btnToday.setOnClickListener{
            start.date = Date().time
            end.date = Date().time
            setDates(start.date, end.date)
        }

        btnToday.performClick()

        //sets up the button to select all the dates
        btnAll.setOnClickListener{
            start.date = start.minDate
            end.date = end.maxDate
            setDates(start.date, end.date)
        }

        //gives the start and end date calendar view a listener to track date selection
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

        //sets up display chart listener to go to activity to display the charts
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

    /** Automatically inflates the options menu to be a part of the toolbar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_date_picker, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Depending on the item clicked, performs a specific task.
     * Automatically called whenever the user clicks an options menu item.
     *
     * @param [item] The options [MenuItem] that the user clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_display_help){ //navigate to HelpActivity with instructions for this activity
            val data = Intent(this, HelpActivity::class.java).apply{
                putExtra("source", "DatePickerActivity")
            }
            startActivity(data)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Sets the date variables.
     *
     * @param [start] The date selected on the [CalendarView] for the start date
     * @param [end] The date selected on the [CalendarView] for the end date
     */
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
